package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class ItemDto
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = {Create.class}, message = ("поле name не должно быть пустым"))
    private String name;

    @NotBlank(groups = {Create.class}, message = ("поле description не должно быть пустым"))
    private String description;

    @NotNull(groups = {Create.class}, message = ("поле available должно быть заполнено"))
    private Boolean available;

    private Long requestId;
}
