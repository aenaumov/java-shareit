package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.UserDto;

import java.util.List;

/**
 * Controller end-point "/users"
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Добавление нового user
     */
    @PostMapping
    public UserDto post(@RequestBody UserDto userDto) {
        log.info("POST user {}", userDto);
        return userService.add(userDto);
    }

    /**
     * Обновление user
     */
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto,
                                                      @PathVariable Long id) {
        log.info("PATCH user {} c id {}", userDto, id);
        return userService.update(userDto, id);
    }

    /**
     * Получение user по id
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("GET user by id={}", id);
        return userService.getOne(id);
    }

    /**
     * Получение всех user
     */
    @GetMapping
    public List<UserDto> getAll() {
        log.info("GET all users");
        return userService.getAll();
    }

    /**
     * Удаление user по id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("DELETE user by id={}", id);
        userService.delete(id);
    }

}
