package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class for testing ItemDto to Json
 */
@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void userDtoTest() throws Exception {

        ItemDto itemDto = new ItemDto(
                1L,
                "item_1",
                "description_1",
                true,
                null
        );

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
    }

}

