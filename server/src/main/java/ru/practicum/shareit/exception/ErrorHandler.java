package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

/*    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFound(final UserNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }*/

/*    @ExceptionHandler
    public ResponseEntity<String> handleItemNotFound(final ItemNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }*/

/*    @ExceptionHandler
    public ResponseEntity<String> handleBookingNotFound(final BookingNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }*/

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotOwnerOfItem(final UserNotOwnerOfItemException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleException(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);
        return new ResponseEntity<>(
                "Произошла непредвиденная ошибка.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleArgumentNotValid(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(
                String.format("Ошибка с полем \"%s\".", e.getParameter()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemBookingNotAvailable(final ItemBookingNotAvailableException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

/*    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotCorrectEnum(final NotCorrectEnumException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }*/

    @ExceptionHandler
    public ResponseEntity<String> handleBookingChangeStatus(final BookingChangeStatusException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBookerIsOwner(final BookerIsOwnerException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAuthorNotBookerOfItem(final AuthorNotBookerOfItemException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

/*    @ExceptionHandler
    public ResponseEntity<String> handleItemRequestNotFound(final ItemRequestNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND);
    }*/
}