package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Class for testing BookingServiceImpl
 */
@MockitoSettings(strictness = Strictness.WARN)
class BookingServiceImplTest {

    private BookingServiceImpl bookingService;

    private BookingRepository bookingRepository;

    private UserRepository userRepository;

    private ItemRepository itemRepository;

    private BookingMapper bookingMapper;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private BookingDtoCreate bookingDtoCreate;


    @BeforeEach
    void init() {
        bookingService = new BookingServiceImpl();
        bookingMapper = new BookingMapper();
        bookingRepository = Mockito.mock(BookingRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);

        ReflectionTestUtils.setField(bookingService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(bookingService, "userRepository", userRepository);
        ReflectionTestUtils.setField(bookingService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(bookingService, "bookingMapper", bookingMapper);

        user1 = new User(1L, "user_1", "user_1@email.com");
        user2 = new User(2L, "user_2", "user_2@email.com");
        user3 = new User(3L, "user_3", "user_3@email.com");

        item1 = new Item(1L, user1, "item_1", "description item_1", true, null);
        item2 = new Item(2L, user2, "item_2", "description item_2", true, null);

        booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item1, user3, BookingStatus.APPROVED);
        booking2 = new Booking(2L, LocalDateTime.now(), LocalDateTime.now(), item2, user3, BookingStatus.APPROVED);

        bookingDtoCreate = new BookingDtoCreate(LocalDateTime.now(), LocalDateTime.now(), 1L);

    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }


    @Test
    void addBookingTestOk() {

        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item1));

        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(user3));

        when(bookingRepository.getBookingsByItemIdAndEndTimeIsAfter(eq(1L), any()))
                .thenReturn(Collections.emptyList());

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking1);

        BookingDto test = bookingService.add(bookingDtoCreate, 3L);

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(3L);
        verify(bookingRepository, times(1))
                .getBookingsByItemIdAndEndTimeIsAfter(eq(1L), any());
        verify(bookingRepository, times(1)).save(any(Booking.class));

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals(1L, test.getItem().getId());
        assertEquals(3L, test.getBooker().getId());

    }

    @Test
    void addBookingWhenBookerIsOwner() {

        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item1));

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        assertThrows(BookerIsOwnerException.class,
                () -> bookingService.add(bookingDtoCreate, 1L));

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void addBookingWhenItemBookingNotAvailableException() {

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Item(2L, user2, "item_2", "description item_2",
                                false, null)));
        assertThrows(ItemBookingNotAvailableException.class,
                () -> bookingService.add(bookingDtoCreate, 1L));

        verify(itemRepository, times(1)).findById(1L);

    }


    @Test
    void updateBookingTestWhenBookingChangeStatusException() {

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking1));

        BookingChangeStatusException exception = assertThrows(BookingChangeStatusException.class,
                () -> bookingService.update(1L, 1L, true));

        verify(bookingRepository, times(1)).findById(1L);

        assertEquals("try to change booking 1 status after Approve", exception.getMessage());
    }

    @Test
    void updateBookingTestWhenUserNotOwnerOfItemException() {

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking1));

        UserNotOwnerOfItemException exception = assertThrows(UserNotOwnerOfItemException.class,
                () -> bookingService.update(1L, 2L, true));

        verify(bookingRepository, times(1)).findById(1L);

        assertEquals("user c id 2 не хозяин", exception.getMessage());
    }

    @Test
    void updateBookingTestWhenBookingNotFoundException() {

        when(bookingRepository.findById(1L))
                .thenThrow(new NotFoundException("booking c id 1 не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.update(1L, 1L, true));

        verify(bookingRepository, times(1)).findById(1L);

        assertEquals("booking c id 1 не найден", exception.getMessage());
    }

    @Test
    void updateBookingTestWhenApprovedByOwner() {

        booking1.setStatus(null);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking1));

        BookingDto test = bookingService.update(1L, 1L, true);

        verify(bookingRepository, times(1)).findById(1L);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("APPROVED", test.getStatus());
    }

    @Test
    void updateBookingTestWhenRejectedByOwner() {

        booking1.setStatus(null);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking1));

        BookingDto test = bookingService.update(1L, 1L, false);

        verify(bookingRepository, times(1)).findById(1L);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("REJECTED", test.getStatus());
    }

    @Test
    void getBookingByIdTestWhenUserNotOwnerOrBooker() {

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking1));

        UserNotOwnerOfItemException exception = assertThrows(UserNotOwnerOfItemException.class,
                () -> bookingService.getBookingById(1L, 4L));

        verify(bookingRepository, times(1)).findById(1L);

        assertEquals("user c id 4 не имеет доступа к booking id 1", exception.getMessage());

    }

    @Test
    void getAllBookerBookingsTestWhenUserNotFoundException() {

        when(userRepository.existsById(3L))
                .thenThrow(new NotFoundException("user c id 3 не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookerBookings(3L, BookingState.ALL, 0, 10));

        verify(userRepository, times(1)).existsById(3L);
        assertEquals("user c id 3 не найден", exception.getMessage());

    }

    @Test
    void getAllBookerBookingsWhenBookingStateAllTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByBooker_Id(eq(3L), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.ALL, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1)).getBookingsByBooker_Id(eq(3L), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllBookerBookingsWhenBookingStateCurrentTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getCurrentBookingsForBooker(eq(3L), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.CURRENT, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1))
                .getCurrentBookingsForBooker(eq(3L), any(), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllBookerBookingsWhenBookingStatePastTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByBooker_IdAndEndBefore(eq(3L), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.PAST, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1))
                .getBookingsByBooker_IdAndEndBefore(eq(3L), any(), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllBookerBookingsWhenBookingStateFutureTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByBooker_IdAndStartAfter(eq(3L), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.FUTURE, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1))
                .getBookingsByBooker_IdAndStartAfter(eq(3L), any(), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllBookerBookingsWhenBookingStateWaitingTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByBooker_IdAndStatusEquals(eq(3L), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.WAITING, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1))
                .getBookingsByBooker_IdAndStatusEquals(eq(3L), any(), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllBookerBookingsWhenBookingStateRejectedTestOk() {

        when(userRepository.existsById(3L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByBooker_IdAndStatusEquals(eq(3L), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDto> list = bookingService.getAllBookerBookings(3L, BookingState.REJECTED, 0, 10);

        verify(userRepository, times(1)).existsById(3L);
        verify(bookingRepository, times(1))
                .getBookingsByBooker_IdAndStatusEquals(eq(3L), any(), any());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStateAllTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByItem_Owner_Id(eq(1L), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.ALL, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1)).getBookingsByItem_Owner_Id(eq(1L), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStateCurrentTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getCurrentBookingsForOwner(eq(1L), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.CURRENT, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1)).getCurrentBookingsForOwner(eq(1L), any(), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStatePastTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByItem_Owner_IdAndEndBefore(eq(1L), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.PAST, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1))
                .getBookingsByItem_Owner_IdAndEndBefore(eq(1L), any(), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStateFutureTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByItem_Owner_IdAndStartAfter(eq(1L), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.FUTURE, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1))
                .getBookingsByItem_Owner_IdAndStartAfter(eq(1L), any(), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStateWaitingTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByItem_Owner_IdAndStatusEquals(eq(1L), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.WAITING, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1))
                .getBookingsByItem_Owner_IdAndStatusEquals(eq(1L), any(), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }

    @Test
    void getAllOwnerBookingsWhenBookingStateRejectedTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(bookingRepository.getBookingsByItem_Owner_IdAndStatusEquals(eq(1L), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDto> list = bookingService.getAllOwnerBookings(1L, BookingState.REJECTED, 0, 10);

        verify(userRepository, times(1)).existsById(1L);
        verify(bookingRepository, times(1))
                .getBookingsByItem_Owner_IdAndStatusEquals(eq(1L), any(), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(3L, list.get(0).getBooker().getId());

    }
}