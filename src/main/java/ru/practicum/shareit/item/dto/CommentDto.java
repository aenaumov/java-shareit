package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Class CommentDtoInfo
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
