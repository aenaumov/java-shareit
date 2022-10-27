package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.common.ShareItPageRequest;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto add(BookingDtoCreate bookingDtoCreate, Long bookerId) {
        final Long itemId = bookingDtoCreate.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item c id %d не найден", itemId)));
        if (!item.getAvailable()) {
            throw new ItemBookingNotAvailableException(
                    String.format("item c id %d не доступен для бронирования", itemId));
        }
        User booker = getUserById(bookerId);
        if (item.getOwner().getId().equals(bookerId)) {
            throw new BookerIsOwnerException("owner couldn't book his own item");
        }
        Booking booking = bookingMapper.toBooking(bookingDtoCreate, booker, item);
        checkBookingTimeAvailable(item, booking);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }


    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getBookingById(bookingId);
        final Long ownerId = getOwnerId(booking);
        if (!userId.equals(ownerId)) {
            throw new UserNotOwnerOfItemException(String.format("user c id %d не хозяин", userId));
        }
        if (booking.getStatus() != null && booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingChangeStatusException(
                    String.format("try to change booking %d status after Approve", bookingId));
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        final Booking booking = getBookingById(bookingId);
        final Long ownerId = getOwnerId(booking);
        final Long bookerId = booking.getBooker().getId();
        if (!userId.equals(ownerId) && !userId.equals(bookerId)) {
            throw new UserNotOwnerOfItemException(
                    String.format("user c id %d не имеет доступа к booking id %d", userId, bookingId));
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookerBookings(Long userId, BookingState state, Integer from, Integer size) {
        isUserExist(userId);
        final LocalDateTime time = LocalDateTime.now();
        final Pageable pageable = getPageableParam(from, size);
        return bookingMapper.bookingDtoList(getAllBookerBookings(userId, state, pageable, time));
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long userId, BookingState state, Integer from, Integer size) {
        isUserExist(userId);
        final LocalDateTime time = LocalDateTime.now();
        final Pageable pageable = getPageableParam(from, size);
        return bookingMapper.bookingDtoList(getAllOwnerBookings(userId, state, pageable, time));
    }

    private void checkBookingTimeAvailable(Item item, Booking booking) {
        LocalDateTime startTimeBooking = booking.getStart();
        LocalDateTime endTimeBooking = booking.getEnd();
        List<Booking> list = bookingRepository
                .getBookingsByItemIdAndEndTimeIsAfter(item.getId(), startTimeBooking);
        long count = list.stream()
                .filter(b -> (b.getEnd().isBefore(endTimeBooking)
                        || b.getStart().isBefore(endTimeBooking)
                        || b.getStart().isBefore(startTimeBooking)))
                .count();
        if (count > 0) {
            throw new ItemBookingNotAvailableException(
                    String.format("item c id %d не доступен для бронирования", item.getId()));
        }
    }

    private Pageable getPageableParam(Integer from, Integer size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        return new ShareItPageRequest(from, size, sort);
    }

    private List<Booking> getAllOwnerBookings(Long userId, BookingState state,
                                              Pageable pageable, LocalDateTime time) {
        switch (state) {
            case CURRENT:
                return bookingRepository.getCurrentBookingsForOwner(userId, time, pageable);
            case PAST:
                return bookingRepository.getBookingsByItem_Owner_IdAndEndBefore(userId, time, pageable);
            case FUTURE:
                return bookingRepository.getBookingsByItem_Owner_IdAndStartAfter(userId, time, pageable);
            case WAITING:
                return bookingRepository
                        .getBookingsByItem_Owner_IdAndStatusEquals(userId, BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository
                        .getBookingsByItem_Owner_IdAndStatusEquals(userId, BookingStatus.REJECTED, pageable);
            default:
                return bookingRepository.getBookingsByItem_Owner_Id(userId, pageable);
        }
    }

    private List<Booking> getAllBookerBookings(Long userId, BookingState state,
                                               Pageable pageable, LocalDateTime time) {
        switch (state) {
            case CURRENT:
                return bookingRepository.getCurrentBookingsForBooker(userId, time, pageable);
            case PAST:
                return bookingRepository.getBookingsByBooker_IdAndEndBefore(userId, time, pageable);
            case FUTURE:
                return bookingRepository.getBookingsByBooker_IdAndStartAfter(userId, time, pageable);
            case WAITING:
                return bookingRepository
                        .getBookingsByBooker_IdAndStatusEquals(userId, BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository
                        .getBookingsByBooker_IdAndStatusEquals(userId, BookingStatus.REJECTED, pageable);
            default:
                return bookingRepository.getBookingsByBooker_Id(userId, pageable);
        }
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("booking c id %d не найден", bookingId)));
    }

    private Long getOwnerId(Booking booking) {
        return booking.getItem().getOwner().getId();
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("user c id %d не найден", id)));
    }

    private void isUserExist(Long idUser) {
        if (!userRepository.existsById(idUser))
            throw new NotFoundException(String.format("user c id %d не найден", idUser));
    }
}
