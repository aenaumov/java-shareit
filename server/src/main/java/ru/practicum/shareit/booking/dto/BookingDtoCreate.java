package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Class BookingDtoCreate
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoCreate {

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
