package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Patch;
import ru.practicum.shareit.user.dto.UserDtoCreate;


/**
 * Controller end-point "/users"
 */

@Slf4j
@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserClient userClient;

    /**
     * Добавление нового user
     */
    @PostMapping
    public ResponseEntity<Object> post(@Validated(Create.class) @RequestBody UserDtoCreate userDto) {
        log.info("POST user {}", userDto);
        return userClient.addUser(userDto);
    }

    /**
     * Обновление user
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Patch.class) @RequestBody UserDtoCreate userDto,
                          @PathVariable Long id) {
        log.info("PATCH user {} c id {}", userDto, id);
        return userClient.updateUser(userDto, id);
    }

    /**
     * Получение user по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info("GET user by id={}", id);
        return userClient.getOneUser(id);
    }

    /**
     * Получение всех user
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET all users");
        return userClient.getAllUsers();
    }

    /**
     * Удаление user по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("DELETE user by id={}", id);
        return userClient.deleteUser(id);
    }

}
