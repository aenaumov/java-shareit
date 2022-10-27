package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class for testing UserDto to Json
 */
@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void userDtoTest() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "user_1",
                "user_1@email.com"
        );

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }
}
