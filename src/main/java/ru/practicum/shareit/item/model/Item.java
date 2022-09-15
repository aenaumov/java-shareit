package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class Item
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;
    private Long idOwner;
    private String name;
    private String description;
    private Boolean available;
}
