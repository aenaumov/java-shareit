package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class ItemDtoLastNext
 * владелец видет даты последнего и ближайшего следующего бронирования для каждой вещи
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoInfo {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        Long id;
        LocalDateTime start;
        LocalDateTime end;
        Long bookerId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        Long id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
