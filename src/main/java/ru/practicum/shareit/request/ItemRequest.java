package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

/**
 * Class ItemRequest
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    Long id;
    String description;
    User requestor;
    String created;
}
