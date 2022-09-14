package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.Create;

import java.util.Collection;

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
                        @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("POST item {} by user id {}", itemDto, idUser);
        return itemService.add(itemDto, idUser);
    }

    /**
     * Обновление Item владельцем
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("PATCH item {} c id {} user id {}", itemDto, itemId, idUser);
        return itemService.update(itemDto, itemId, idUser);
    }

    /**
     * Получение Item по id
     */
    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET item by id={}", itemId);
        return itemService.getOne(itemId);
    }

    /**
     * Получение всех Item владельцем
     */
    @GetMapping
    public Collection<ItemDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET all items by owner {}", idUser);
        return itemService.getAllByOwner(idUser);
    }

    /**
     * Удаление Item по id владельцем
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id,
                           @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("DELETE item by id={}", id);
        itemService.delete(id, idUser);
    }

    /**
     * Поиск Item по тексту
     */
    @GetMapping("/search")
    public Collection<ItemDto> searchItemByText(@RequestParam String text) {
        log.info("GET items по поиску по тексту {}", text);
        return itemService.searchByText(text);
    }
}
