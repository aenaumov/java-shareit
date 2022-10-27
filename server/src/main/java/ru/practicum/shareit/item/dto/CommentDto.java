package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Class CommentDto
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
