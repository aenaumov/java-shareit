package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

/**
 * Controller end-point "/bookings"
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Добавление бронирования вещи
     */
    @PostMapping
    public BookingDto post(@RequestBody BookingDtoCreate bookingDtoCreate,
                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST booking {} by user id {}", bookingDtoCreate, userId);
        return bookingService.add(bookingDtoCreate, userId);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи
     */
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestParam Boolean approved,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH booking id {} by user id {}", bookingId, userId);
        return bookingService.update(bookingId, userId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET booking {} by owner or user {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     */
    @GetMapping
    public List<BookingDto> getAllUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam BookingState state,
            @RequestParam Integer from,
            @RequestParam Integer size) {
        log.info("GET all bookings by user {}", userId);
        return bookingService.getAllBookerBookings(userId, state, from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(
            @RequestParam BookingState state,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size) {
        log.info("GET all bookings by owner {}", userId);
        return bookingService.getAllOwnerBookings(userId, state, from, size);
    }
}
