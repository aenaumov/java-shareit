package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class for testing BookingDto to Json
 */
@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void userDtoInfoTest() throws Exception {

        BookingDto dto = new BookingDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "APPROVED",
                new BookingDto.ItemDto(2L, "item_2"),
                new BookingDto.UserDto(3L)
        );

        JsonContent<BookingDto> result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item_2");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(3);
    }
}