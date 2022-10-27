package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Class for unit testing UserServiceImpl
 */

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userService;

    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void init() {

        userService = new UserServiceImpl();
        userMapper = new UserMapper();

        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "userMapper", userMapper);

        user = new User(1L, "user_1", "user_1@email.com");
        userDto = new UserDto(1L, "user_1", "user_1@email.com");
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addUserTestOk() {

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto test = userService.add(userDto);

        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("user_1", test.getName());
        assertEquals("user_1@email.com", test.getEmail());
    }

    @Test
    void updateUserTestOk() {

        UserDto userDtoUpdated = new UserDto(1L, "user_updated", "updated@email.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        UserDto test = userService.update(userDtoUpdated, 1L);

        verify(userRepository, times(1)).findById(1L);
        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("user_updated", test.getName());
        assertEquals("updated@email.com", test.getEmail());
    }

    @Test
    void getOneUserTestOk() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        UserDto test = userService.getOne(1L);

        verify(userRepository, times(1)).findById(1L);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals("user_1", test.getName());
        assertEquals("user_1@email.com", test.getEmail());

    }

    @Test
    void getOneUserWhenUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException("user c id 1 не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getOne(1L));

        verify(userRepository, times(1)).findById(1L);

        assertEquals("user c id 1 не найден", exception.getMessage());


    }

    @Test
    void deleteTest() {

        doNothing().when(userRepository).deleteById(1L);
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllTest() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> list = userService.getAll();

        verify(userRepository, times(1)).findAll();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals("user_1", list.get(0).getName());
        assertEquals("user_1@email.com", list.get(0).getEmail());

    }
}