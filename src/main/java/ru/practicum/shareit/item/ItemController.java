package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Controller end-point "/items"
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Добавление новой Item владельцем
     */
    @PostMapping
    public ItemDto post(@Validated(Create.class) @RequestBody ItemDto itemDto,
                        @NotBlank @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("POST item {} by user id {}", itemDto, idUser);
        return itemService.addItem(itemDto, idUser);
    }

    /**
     * Обновление Item владельцем
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("PATCH item {} c id {} user id {}", itemDto, itemId, idUser);
        return itemService.updateItem(itemDto, itemId, idUser);
    }

    /**
     * Получение Item по id
     */
    @GetMapping("/{itemId}")
    public ItemDtoInfo getItem(@PathVariable Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET user id {} asks for item by id={}", idUser, itemId);
        return itemService.getOneItem(itemId, idUser);
    }

    /**
     * Получение всех Item владельцем
     */
    @GetMapping
    public List<ItemDtoInfo> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET all items by owner {}", idUser);
        return itemService.getAllItemsByOwner(idUser);
    }

    /**
     * Удаление Item по id владельцем
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id,
                           @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("DELETE item by id={}", id);
        itemService.deleteItem(id, idUser);
    }

    /**
     * Поиск Item по тексту
     */
    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        log.info("GET items по поиску по тексту {}", text);
        return itemService.searchItemsByText(text);
    }

    /**
     * Добавление нового comment
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Validated @RequestBody CommentDtoCreate commentDtoCreate,
                                 @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("POST user {} adds comment {} for item {}", idUser, commentDtoCreate, itemId);
        return itemService.addComment(idUser, itemId, commentDtoCreate);
    }
}
