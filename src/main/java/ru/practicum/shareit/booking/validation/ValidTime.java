package ru.practicum.shareit.booking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Интерфейс валидации "start" and "end" в BookingDtoCreate
 */
@Constraint(validatedBy = ValidTimeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidTime {

    String message() default "Start time couldn't be after end time!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
