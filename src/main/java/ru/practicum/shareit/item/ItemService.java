package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * Интерфейс для работы с item
 */
public interface ItemService {

    /**
     * создать item
     */
    ItemDto add(ItemDto itemDto, Long idUser);

    /**
     * обновить item
     */
    ItemDto update(ItemDto itemDto, Long itemId, Long idUser);

    /**
     * получить item по id
     */
    ItemDto getOne(Long itemId);

    /**
     * удалить item
     */
    void delete(Long itemId, Long userId);

    /**
     * получить всех item по id user
     */
    Collection<ItemDto> getAllByOwner(Long userId);

    /**
     * поиск item по тексту
     */
    Collection<ItemDto> searchByText(String text);
}
