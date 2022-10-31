package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;

import java.util.List;

/**
 * Controller end-point "/requests"
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @Autowired
    private ItemRequestService itemRequestService;

    /**
     * добавить новый запрос вещи
     */
    @PostMapping
    public ItemRequestDto post(@RequestBody ItemRequestDto itemRequestDto,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST request {} by user id {}", itemRequestDto, userId);
        return itemRequestService.addRequest(itemRequestDto, userId);
    }

    /**
     * получить список своих запросов вместе с данными об ответах на них
     */
    @GetMapping
    public List<ItemRequestDtoInfo> getAllByRequester(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET all requests by requester {}", userId);
        return itemRequestService.getAllUserRequests(userId);
    }

    /**
     * получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<ItemRequestDtoInfo> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam Integer from,
                                           @RequestParam Integer size) {
        log.info("GET all requests by user {}", userId);
        return itemRequestService.getAllRequests(userId, from, size);
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах
     */
    @GetMapping("/{requestId}")
    public ItemRequestDtoInfo getByRequester(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        log.info("GET request {} by owner {}", requestId, userId);
        return itemRequestService.getRequestById(userId, requestId);
    }

}
