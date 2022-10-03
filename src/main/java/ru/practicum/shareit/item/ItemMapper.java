package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для конвертации Item в DTO и обратно
 */
@Component
public class ItemMapper {
    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item toItem(ItemDto itemDto, Long itemId, User user) {
        return new Item(itemId, user, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ItemDtoInfo toItemDtoInfo(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        return new ItemDtoInfo(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(),
                lastBooking != null ? new ItemDtoInfo.BookingDto(lastBooking.getId(),
                        lastBooking.getStart(),
                        lastBooking.getEnd(),
                        lastBooking.getBooker().getId()) : null,
                nextBooking != null ? new ItemDtoInfo.BookingDto(nextBooking.getId(),
                        nextBooking.getStart(),
                        nextBooking.getEnd(),
                        nextBooking.getBooker().getId()) : null,
                comments != null ? commentDtoList(comments) : null);
    }

    private List<ItemDtoInfo.CommentDto> commentDtoList(List<Comment> comments) {
        final List<ItemDtoInfo.CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(new ItemDtoInfo.CommentDto(comment.getId(),
                    comment.getText(),
                    comment.getAuthor().getName(),
                    comment.getCreated()));
        }
        return commentDtoList;
    }
}
