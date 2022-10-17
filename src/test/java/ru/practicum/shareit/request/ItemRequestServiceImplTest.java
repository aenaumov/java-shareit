package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class for testing ItemRequestServiceImpl
 */
@MockitoSettings(strictness = Strictness.WARN)
class ItemRequestServiceImplTest {

    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestRepository itemRequestRepository;

    private UserRepository userRepository;

    private ItemRepository itemRepository;

    private ItemRequestMapper itemRequestMapper;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private ItemRequest itemRequest1;
    private ItemRequestDto itemRequestDto1;

    @BeforeEach
    void init() {
        itemRequestService = new ItemRequestServiceImpl();
        itemRequestMapper = new ItemRequestMapper();
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);

        ReflectionTestUtils.setField(itemRequestService, "itemRequestRepository", itemRequestRepository);
        ReflectionTestUtils.setField(itemRequestService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRequestMapper", itemRequestMapper);

        user1 = new User(1L, "user_1", "user_1@email.com");
        user2 = new User(2L, "user_2", "user_2@email.com");

        item1 = new Item(1L, user1, "item_1", "description item_1", true, null);
        item2 = new Item(2L, user2, "item_2", "description item_2", true, null);

        itemRequest1 = new ItemRequest(1L, "request description", user1, LocalDateTime.now());
        itemRequestDto1 = new ItemRequestDto(1L, "request description", LocalDateTime.now());

    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository, itemRepository, itemRequestRepository);
    }

    @Test
    void addRequestTestOk() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user1));

        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest1);

        ItemRequestDto test = itemRequestService.addRequest(itemRequestDto1, 1L);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("request description", test.getDescription());

    }

    @Test
    void getAllUserRequestsTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(itemRequestRepository.getItemRequestsByRequestor_Id(eq(1L), any()))
                .thenReturn(List.of(itemRequest1));

        when(itemRepository.getItemsByRequestId(1L))
                .thenReturn(List.of(item1, item2));

        List<ItemRequestDtoInfo> list = itemRequestService.getAllUserRequests(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(itemRequestRepository, times(1)).getItemRequestsByRequestor_Id(eq(1L), any());
        verify(itemRepository, times(1)).getItemsByRequestId(1L);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("request description", list.get(0).getDescription());
        assertEquals(2, list.get(0).getItems().size());

    }

    @Test
    void getAllRequestsTestOk() {

        when(userRepository.existsById(2L))
                .thenReturn(true);

        when(itemRequestRepository.getItemRequestsByRequestorIdIsNot(eq(2L), any()))
                .thenReturn(List.of(itemRequest1));

        when(itemRepository.getItemsByRequestId(1L))
                .thenReturn(List.of(item1, item2));

        List<ItemRequestDtoInfo> list = itemRequestService.getAllRequests(2L, 0, 10);

        verify(userRepository, times(1)).existsById(2L);
        verify(itemRequestRepository, times(1))
                .getItemRequestsByRequestorIdIsNot(eq(2L), any());
        verify(itemRepository, times(1)).getItemsByRequestId(1L);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("request description", list.get(0).getDescription());
        assertEquals(2, list.get(0).getItems().size());

    }

    @Test
    void getRequestByIdTestOk() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemRequest1));

        when(itemRepository.getItemsByRequestId(1L))
                .thenReturn(List.of(item1, item2));

        ItemRequestDtoInfo test = itemRequestService.getRequestById(1L, 1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).getItemsByRequestId(1L);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("request description", test.getDescription());
        assertEquals(2, test.getItems().size());
    }

    @Test
    void getRequestByIdWhenItemRequestNotFoundExceptionTest() {
        when(userRepository.existsById(1L))
                .thenReturn(true);
        when(itemRequestRepository.findById(1L))
                .thenThrow(new ItemRequestNotFoundException("request c id 1 не найден"));

        ItemRequestNotFoundException exception = assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestService.getRequestById(1L, 1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(itemRequestRepository, times(1)).findById(1L);

        assertEquals("request c id 1 не найден", exception.getMessage());
    }

    @Test
    void getRequestByIdWhenUserNotFoundExceptionTest() {
        when(userRepository.existsById(1L))
                .thenThrow(new UserNotFoundException("request c id 1 не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.getRequestById(1L, 1L));

        verify(userRepository, times(1)).existsById(1L);

        assertEquals("request c id 1 не найден", exception.getMessage());
    }
}