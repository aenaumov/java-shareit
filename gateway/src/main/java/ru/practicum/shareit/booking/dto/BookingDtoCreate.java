package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validation.ValidTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * Class BookingDtoCreate
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ValidTime
public class BookingDtoCreate {

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private Long itemId;
}
