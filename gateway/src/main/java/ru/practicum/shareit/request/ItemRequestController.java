package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Controller end-point "/requests"
 */
@Slf4j
@Controller
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @Autowired
    private ItemRequestClient itemRequestClient;

    /**
     * добавить новый запрос вещи
     */
    @PostMapping
    public ResponseEntity<Object> post(@Validated @RequestBody ItemRequestDtoCreate itemRequestDto,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST request {} by user id {}", itemRequestDto, userId);
        return itemRequestClient.addRequest(itemRequestDto, userId);
    }

    /**
     * получить список своих запросов вместе с данными об ответах на них
     */
    @GetMapping
    public ResponseEntity<Object> getAllByRequester(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET all requests by requester {}", userId);
        return itemRequestClient.getAllUserRequests(userId);
    }

    /**
     * получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(required = false, defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Positive Integer size) {
        log.info("GET all requests by user {}", userId);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequester(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long requestId) {
        log.info("GET request {} by owner {}", requestId, userId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

}
