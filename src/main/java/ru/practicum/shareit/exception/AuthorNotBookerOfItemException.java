package ru.practicum.shareit.exception;

public class AuthorNotBookerOfItemException extends RuntimeException {
    public AuthorNotBookerOfItemException(String message) {
        super(message);
    }
}
