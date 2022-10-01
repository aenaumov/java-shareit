package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validation.ValidTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;

    @NotNull
    Long itemId;
}