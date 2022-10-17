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

    private Long requestId;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long bookerId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long id;
        private String text;
        private String authorName;
        private LocalDateTime created;
    }
}
