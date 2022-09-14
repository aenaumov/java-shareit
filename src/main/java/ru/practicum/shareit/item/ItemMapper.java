package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс для конвертации Item в DTO и обратно
 */
@Component
public class ItemMapper {
    public ItemDto toDto(Item item) {
        final Long id = item.getId();
        final String name = item.getName();
        final String description = item.getDescription();
        final Boolean available = item.getAvailable();
        return new ItemDto(id, name, description, available);
    }

    public Item toItem(ItemDto itemDto, Long itemId, Long userId) {
        final String name = itemDto.getName();
        final String description = itemDto.getDescription();
        final Boolean available = itemDto.getAvailable();
        return new Item(itemId, userId, name, description, available);
    }

    public Collection<ItemDto> toItemDtoCollection(Collection<Item> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
