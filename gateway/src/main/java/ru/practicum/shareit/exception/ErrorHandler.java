package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleArgumentNotValid(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(
                String.format("Ошибка с полем \"%s\".", e.getParameter()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotCorrectEnum(final IllegalArgumentException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);
        return new ResponseEntity<>(
                "Произошла непредвиденная ошибка.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}