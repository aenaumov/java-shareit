package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для конвертации User в DTO и обратно
 */
@Component
public class UserMapper {
    public UserDto toDto(User user) {
        final Long id = user.getId();
        final String name = user.getName();
        final String email = user.getEmail();
        return new UserDto(id, name, email);
    }

    public User toUser(UserDto userDto) {
        final String name = userDto.getName();
        final String email = userDto.getEmail();
        return new User(null, name, email);
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
