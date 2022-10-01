package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.NotCorrectEnumException;

import java.util.List;
import java.util.Optional;

/**
 * Controller end-point "/bookings"
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    /**
     * Добавление бронирования вещи
     */
    @PostMapping
    public BookingDto post(@Validated @RequestBody BookingDtoCreate bookingDtoCreate,
                           @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("POST booking {} by user id {}", bookingDtoCreate, idUser);
        return bookingService.add(bookingDtoCreate, idUser);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи
     */
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestParam Boolean approved,
                             @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("POST booking id {} by user id {}", bookingId, idUser);
        return bookingService.update(bookingId, idUser, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET booking {} by owner or user {}", bookingId, idUser);
        return bookingService.getBookingById(bookingId, idUser);
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     */
    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET all bookings by user {}", idUser);
        Optional<BookingState> stateOptional = BookingState.from(state);
        BookingState bookingState = stateOptional
                .orElseThrow(() -> new NotCorrectEnumException("Unknown state: " + state));
        return bookingService.getAllBookerBookings(idUser, bookingState);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("GET all bookings by owner {}", idUser);
        Optional<BookingState> stateOptional = BookingState.from(state);
        BookingState bookingState = stateOptional
                .orElseThrow(() -> new NotCorrectEnumException("Unknown state: " + state));
        return bookingService.getAllOwnerBookings(idUser, bookingState);

    }
}
