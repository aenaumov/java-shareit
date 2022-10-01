package ru.practicum.shareit.exception;

public class ItemBookingNotAvailableException extends RuntimeException {
    public ItemBookingNotAvailableException(String message) {
        super(message);
    }
}
