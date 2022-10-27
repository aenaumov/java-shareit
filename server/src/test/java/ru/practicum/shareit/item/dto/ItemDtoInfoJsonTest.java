package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class for testing ItemDtoInfo to Json
 */
@JsonTest
public class ItemDtoInfoJsonTest {

    @Autowired
    private JacksonTester<ItemDtoInfo> json;

    @Test
    void userDtoInfoTest() throws Exception {

        ItemDtoInfo dto = new ItemDtoInfo(
                1L,
                "item_1",
                "description_1",
                true,
                null,
                new ItemDtoInfo.BookingDto(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        2L
                ),
                new ItemDtoInfo.BookingDto(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        3L
                ),
                List.of(new ItemDtoInfo.CommentDto(
                        3L,
                        "comments",
                        "authorName",
                        LocalDateTime.now()
                ))
        );

        JsonContent<ItemDtoInfo> result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("comments");
    }
}

