package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для конвертации Booking в DTO и обратно
 */
@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus().toString(),
                new BookingDto.ItemDto(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.UserDto(booking.getBooker().getId()));
    }

    public Booking toBooking(BookingDtoCreate bookingDtoCreate, User booker, Item item) {
        return new Booking(
                null,
                bookingDtoCreate.getStart(),
                bookingDtoCreate.getEnd(),
                item,
                booker,
                BookingStatus.WAITING);
    }

    public List<BookingDto> bookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
