package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Controller end-point "/items"
 */
@Slf4j
@Controller
@Validated
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemClient itemClient;

    /**
     * Добавление новой Item владельцем
     */
    @PostMapping
    public ResponseEntity<Object> post(@Validated/*(Create.class)*/ @RequestBody ItemDtoCreate itemDto,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST item {} by user id {}", itemDto, userId);
        return itemClient.addItem(itemDto, userId);
    }

    /**
     * Обновление Item владельцем
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDtoCreate itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH item {} c id {} user id {}", itemDto, itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    /**
     * Получение Item по id
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET user id {} asks for item by id={}", userId, itemId);
        return itemClient.getOneItem(itemId, userId);
    }

    /**
     * Получение всех Item владельцем
     */
    @GetMapping
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(required = false, defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Positive Integer size) {
        log.info("GET all items by owner {}", userId);
        return itemClient.getAllItemsByOwner(userId, from, size);
    }

    /**
     * Удаление Item по id владельцем
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItemByUser(@PathVariable Long id,
                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("DELETE item by id={}", id);
        return itemClient.deleteItem(id, userId);
    }

    /**
     * Поиск Item по тексту
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                          @RequestParam(required = false, defaultValue = "0")
                                          @PositiveOrZero Integer from,
                                          @RequestParam(required = false, defaultValue = "10")
                                          @Positive Integer size) {
        log.info("GET items по поиску по тексту {}", text);
        return itemClient.searchItemsByText(text, from, size);
    }

    /**
     * Добавление нового comment
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Validated @RequestBody CommentDtoCreate commentDtoCreate,
                                 @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST user {} adds comment {} for item {}", userId, commentDtoCreate, itemId);
        return itemClient.addComment(userId, itemId, commentDtoCreate);
    }
}
