package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item add(Item item);

    Item update(Item item);

    Item getOne(Long itemId);

    void delete(Long itemId);

    Collection<Item> getAll();
}
