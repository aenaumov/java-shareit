package ru.practicum.shareit.user.dto;

import lombok.*;

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
    private String name;
    private String email;
}
