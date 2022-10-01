package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidTimeValidator implements ConstraintValidator<ValidTime, BookingDtoCreate> {

    @Override
    public boolean isValid(BookingDtoCreate bookingDtoCreate, ConstraintValidatorContext constraintValidatorContext) {

        final LocalDateTime start = bookingDtoCreate.getStart();
        final LocalDateTime end = bookingDtoCreate.getEnd();
        return start.isBefore(end);
    }
}
