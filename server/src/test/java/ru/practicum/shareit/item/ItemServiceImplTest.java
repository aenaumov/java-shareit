package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.AuthorNotBookerOfItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Class for testing ItemServiceImpl
 */

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    private ItemMapper itemMapper;
    private CommentMapper commentMapper;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private ItemDto itemDto1;
    private Booking lastBooking;
    private Booking nextBooking;
    private Comment comment;
    private CommentDtoCreate commentDtoCreate;


    @BeforeEach
    void init() {
        itemService = new ItemServiceImpl();
        itemMapper = new ItemMapper();
        commentMapper = new CommentMapper();

        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(itemService, "commentRepository", commentRepository);
        ReflectionTestUtils.setField(itemService, "itemMapper", itemMapper);
        ReflectionTestUtils.setField(itemService, "commentMapper", commentMapper);

        user1 = new User(1L, "user_1", "user_1@email.com");
        user2 = new User(2L, "user_2", "user_2@email.com");
        user3 = new User(3L, "user_3", "user_3@email.com");

        item1 = new Item(1L, user1, "item_1", "description item_1", true, null);

        itemDto1 = new ItemDto(1L, "item_1", "description item_1", true, null);

        lastBooking = new Booking(1L, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3),
                item1, user2, BookingStatus.APPROVED);
        nextBooking = new Booking(2L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5),
                item1, user3, BookingStatus.APPROVED);

        comment = new Comment(1L, "text comment", user3, item1, LocalDateTime.now());

        commentDtoCreate = new CommentDtoCreate("text comment");
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository, commentRepository);
    }


    @Test
    void addItemTestOk() {

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item1);

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        ItemDto test = itemService.addItem(itemDto1, 1L);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("item_1", test.getName());
        assertEquals("description item_1", test.getDescription());

    }

    @Test
    void updateItemTestOk() {

        ItemDto itemDto1Updated = new ItemDto(1L, "item_1_updated", "description updated",
                true, null);

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        when(itemRepository.getItemByIdAndOwner(1L, user1))
                .thenReturn(Optional.ofNullable(item1));

        ItemDto test = itemService.updateItem(itemDto1Updated, 1L, 1L);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).getItemByIdAndOwner(1L, user1);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("item_1_updated", test.getName());
        assertEquals("description updated", test.getDescription());

    }

    @Test
    void getOneItemTestOk() {

        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item1));

        when(bookingRepository.findFirstByItem_IdAndStartIsBefore(eq(1L), any(), any()))
                .thenReturn(Optional.ofNullable(lastBooking));

        when(bookingRepository.findFirstByItem_IdAndStartIsAfter(eq(1L), any(), any()))
                .thenReturn(Optional.ofNullable(nextBooking));

        when(commentRepository.findCommentsByItem(item1))
                .thenReturn(List.of(comment));

        ItemDtoInfo test = itemService.getOneItem(1L, 1L);

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStartIsBefore(eq(1L), any(), any());
        verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStartIsAfter(eq(1L), any(), any());
        verify(commentRepository, times(1)).findCommentsByItem(item1);


        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("item_1", test.getName());
        assertEquals("description item_1", test.getDescription());
        assertEquals(2L, test.getLastBooking().getBookerId());
        assertEquals(3L, test.getNextBooking().getBookerId());

    }

    @Test
    void getOneItemWhenItemNotFoundExceptionTest() {

        when(itemRepository.findById(1L))
                .thenThrow(new NotFoundException("item c id 1 не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.getOneItem(1L, 1L));

        verify(itemRepository, times(1)).findById(1L);

        assertEquals("item c id 1 не найден", exception.getMessage());

    }

    @Test
    void deleteItemTestOk() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        doNothing().when(itemRepository).deleteItemByIdAndOwner(1L, user1);

        itemService.deleteItem(1L, 1L);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).deleteItemByIdAndOwner(1L, user1);

    }

    @Test
    void getAllItemsByOwnerTestOk() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        when(itemRepository.getAllByOwner(eq(user1), any()))
                .thenReturn(List.of(item1));

        when(bookingRepository.findFirstByItem_IdAndStartIsBefore(eq(1L), any(), any()))
                .thenReturn(Optional.ofNullable(lastBooking));

        when(bookingRepository.findFirstByItem_IdAndStartIsAfter(eq(1L), any(), any()))
                .thenReturn(Optional.ofNullable(nextBooking));

        when(commentRepository.findCommentsByItem(item1))
                .thenReturn(List.of(comment));

        List<ItemDtoInfo> list = itemService.getAllItemsByOwner(1L, 0, 10);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).getAllByOwner(eq(user1), any());
        verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStartIsBefore(eq(1L), any(), any());
        verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStartIsAfter(eq(1L), any(), any());
        verify(commentRepository, times(1)).findCommentsByItem(item1);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals("item_1", list.get(0).getName());
        assertEquals("description item_1", list.get(0).getDescription());
        assertEquals(2L, list.get(0).getLastBooking().getBookerId());
        assertEquals(3L, list.get(0).getNextBooking().getBookerId());

    }

    @Test
    void searchItemsByTextTestOk() {

        when(itemRepository.searchAvailableItemsByText(eq("%item_1%"), any()))
                .thenReturn(List.of(item1));

        List<ItemDto> list = itemService.searchItemsByText("item_1", 0, 10);

        verify(itemRepository, times(1)).searchAvailableItemsByText(eq("%item_1%"), any());

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals("item_1", list.get(0).getName());
        assertEquals("description item_1", list.get(0).getDescription());
    }

    @Test
    void addCommentTestOk() {

        when(userRepository.findById(2L))
                .thenReturn(Optional.ofNullable(user2));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item1));

        when(bookingRepository.findApprovedBookingsInPastByUser(eq(2L), eq(1L), any()))
                .thenReturn(List.of(lastBooking));

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        CommentDto test = itemService.addComment(2L, 1L, commentDtoCreate);

        verify(userRepository, times(1)).findById(2L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1))
                .findApprovedBookingsInPastByUser(eq(2L), eq(1L), any());
        verify(commentRepository, times(1)).save(any(Comment.class));

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("text comment", test.getText());
        assertEquals("user_3", test.getAuthorName());
    }

    @Test
    void addCommentWhenAuthorNotBookerOfItemExceptionTest() {

        when(userRepository.findById(2L))
                .thenReturn(Optional.ofNullable(user2));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item1));

        when(bookingRepository.findApprovedBookingsInPastByUser(eq(2L), eq(1L), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(AuthorNotBookerOfItemException.class,
                () -> itemService.addComment(2L, 1L, commentDtoCreate));

        verify(userRepository, times(1)).findById(2L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1))
                .findApprovedBookingsInPastByUser(eq(2L), eq(1L), any());
    }
}