package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Class ItemRequestDto
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;

    @NotBlank(groups = {Create.class}, message = ("требуется ввести описание"))
    private String description;
    private LocalDateTime created;
}