package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Patch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Class UserDto
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;

    @NotNull(groups = {Create.class}, message = ("требуется ввести имя"))
    private String name;

    @Email(groups = {Create.class, Patch.class}, regexp = "\\w+@\\w+.\\w+", message = ("email не прошел валидацию"))
    @NotNull(groups = {Create.class}, message = ("требуется ввести e-mail"))
    private String email;
}
