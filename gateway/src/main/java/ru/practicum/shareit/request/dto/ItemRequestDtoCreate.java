package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Class ItemRequestDtoCreate
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoCreate {

    private Long id;

    @NotBlank(message = ("требуется ввести описание"))
    private String description;
    private LocalDateTime created;
}