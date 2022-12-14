package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Class CommentDtoCreate
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoCreate {
    @NotBlank
    @Size(max = 500, message = "длина не более 500 знаков")
    private String text;
}
