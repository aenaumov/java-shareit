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
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private ItemDto item;
    private UserDto booker;

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
