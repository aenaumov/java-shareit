package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class for testing ItemRequestDtoInfo to Json
 */
@JsonTest
class ItemRequestDtoInfoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDtoInfo> json;

    @Test
    void userDtoTest() throws Exception {

        ItemRequestDtoInfo dto = new ItemRequestDtoInfo(
                1L,
                "description_1",
                LocalDateTime.now(),
                List.of(new ItemRequestDtoInfo.ItemDto(
                        2L,
                        "user_2",
                        "description_2",
                        true,
                        null
                ))
        );

        JsonContent<ItemRequestDtoInfo> result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("user_2");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo("description_2");
    }

}