package ru.practicum.shareit.item.dto;

import lombok.*;

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

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
