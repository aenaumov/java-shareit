package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

/**
 * Интерфейс для работы с Booking
 */
public interface BookingService {

    /**
     * создать booking
     */
    BookingDto add(BookingDtoCreate bookingDtoCreate, Long bookerId);

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи
     */
    BookingDto update(Long bookingId, Long userId, Boolean approved);

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     */
    BookingDto getBookingById(Long bookingId, Long userId);

    /**
     * Получение списка всех бронирований текущего пользователя.
     */
    List<BookingDto> getAllBookerBookings(Long userId, BookingState state, Integer from, Integer size);

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    List<BookingDto> getAllOwnerBookings(Long userId, BookingState state, Integer from, Integer size);
}
