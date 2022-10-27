package ru.practicum.shareit.exception;

public class UserNotOwnerOfItemException extends RuntimeException {
    public UserNotOwnerOfItemException(String message) {
        super(message);
    }
}
