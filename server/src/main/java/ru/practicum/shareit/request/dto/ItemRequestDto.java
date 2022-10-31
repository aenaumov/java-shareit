package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String description;
    private LocalDateTime created;
}