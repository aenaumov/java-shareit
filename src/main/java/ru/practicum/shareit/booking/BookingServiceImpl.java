package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto add(BookingDtoCreate bookingDtoCreate, Long bookerId) {
        final Long itemId = bookingDtoCreate.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("item c id %d не найден", itemId)));
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
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
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

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getBookingById(bookingId);
        final Long ownerId = getOwnerId(booking);
        if (!userId.equals(ownerId)) {
            throw new UserNotOwnerOfItemException(String.format("user c id %d не хозяин", userId));
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
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
    public List<BookingDto> getAllBookerBookings(Long userId, BookingState state) {
        isUserExist(userId);
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.getBookingsByBooker(userId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.getCurrentBookingsForBooker(userId, time, sort);
                break;
            case PAST:
                bookings = bookingRepository.getPastBookingsForBooker(userId, time, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.getFutureBookingsForBooker(userId, time, sort);
                break;
            case WAITING:
                bookings = bookingRepository.getBookingsByBookerAndStatus(userId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.getBookingsByBookerAndStatus(userId, BookingStatus.REJECTED, sort);
                break;
        }
        return bookingMapper.bookingDtoList(bookings);
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long userId, BookingState state) {
        isUserExist(userId);
        final Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.getBookingsByOwner(userId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.getCurrentBookingsForOwner(userId, time, sort);
                break;
            case PAST:
                bookings = bookingRepository.getPastBookingsForOwner(userId, time, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.getFutureBookingsForOwner(userId, time, sort);
                break;
            case WAITING:
                bookings = bookingRepository
                        .getBookingsByItemBYOwnerAndStatus(userId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .getBookingsByItemBYOwnerAndStatus(userId, BookingStatus.REJECTED, sort);
                break;
        }
        return bookingMapper.bookingDtoList(bookings);
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("booking c id %d не найден", bookingId)));
    }

    private Long getOwnerId(Booking booking) {
        return booking.getItem().getOwner().getId();
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("user c id %d не найден", id)));
    }

    private void isUserExist(Long idUser) {
        if (!userRepository.existsById(idUser))
            throw new UserNotFoundException(String.format("user c id %d не найден", idUser));
    }
}
