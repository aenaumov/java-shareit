package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;

import java.util.List;

/**
 * Интерфейс для работы с item и comment
 */
public interface ItemService {

    /**
     * создать item
     */
    ItemDto addItem(ItemDto itemDto, Long idUser);

    /**
     * обновить item
     */
    ItemDto updateItem(ItemDto itemDto, Long itemId, Long idUser);

    /**
     * получить item по id
     */
    ItemDtoInfo getOneItem(Long itemId, Long userId);

    /**
     * удалить item
     */
    void deleteItem(Long itemId, Long userId);

    /**
     * получить всех item по id user
     */
    List<ItemDtoInfo> getAllItemsByOwner(Long userId);

    /**
     * поиск item по тексту
     */
    List<ItemDto> searchItemsByText(String text);

    /**
     * добавить comment
     */
    CommentDto addComment(Long userId, Long itemId, CommentDtoCreate commentDtoCreate);
}

