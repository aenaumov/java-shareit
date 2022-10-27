package ru.practicum.shareit.request;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для конвертации Request в DTO и обратно
 */
@Component
public class ItemRequestMapper {

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requestor,
                itemRequestDto.getCreated());
    }

    public ItemRequestDtoInfo toItemRequestDtoInfo(ItemRequest itemRequest, List<Item> items) {
        return new ItemRequestDtoInfo(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getCreated(),
                items != null ? toItemDtoList(items) : null);
    }

    private List<ItemRequestDtoInfo.ItemDto> toItemDtoList(List<Item> items) {
        final List<ItemRequestDtoInfo.ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            itemDtoList.add(new ItemRequestDtoInfo.ItemDto(item.getId(),
                    item.getName(), item.getDescription(), item.getAvailable(), item.getRequestId()));
        }
        return itemDtoList;
    }
}
