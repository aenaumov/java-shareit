package ru.practicum.shareit.user;

import ru.practicum.shareit.dto.UserDto;

import java.util.List;

/**
 * Интерфейс для работы с user
 */
public interface UserService {

    /**
     * создать user
     */
    UserDto add(UserDto userDto);

    /**
     * обновить user
     */
    UserDto update(UserDto userDto, Long id);

    /**
     * получить user по id
     */
    UserDto getOne(Long id);

    /**
     * удалить user по id
     */
    void delete(Long id);

    /**
     * получить всех user
     */
    List<UserDto> getAll();
}
