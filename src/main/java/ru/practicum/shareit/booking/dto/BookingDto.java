package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class BookingDto
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    String status;
    ItemDto item;
    UserDto booker;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDto {
        private Long id;
    }
}
