package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFound(final UserNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemNotFound(final ItemNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotOwnerOfItem(final UserNotOwnerOfItemException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleEmailAlreadyExist(final EmailAlreadyExist e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);
        return new ResponseEntity<>(
                "Произошла непредвиденная ошибка.",
                HttpStatus.BAD_REQUEST);
    }

}
