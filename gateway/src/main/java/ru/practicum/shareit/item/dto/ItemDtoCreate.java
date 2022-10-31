package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class ItemDtoCreate
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoCreate {

    private Long id;

    @NotBlank(message = ("поле name не должно быть пустым"))
    private String name;

    @NotBlank(message = ("поле description не должно быть пустым"))
    private String description;

    @NotNull(message = ("поле available должно быть заполнено"))
    private Boolean available;

    private Long requestId;
}
