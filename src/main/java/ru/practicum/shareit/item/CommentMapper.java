package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс для конвертации Comment в DTO и обратно
 */
@Component
public class CommentMapper {

    public Comment toComment(CommentDtoCreate commentDtoCreate, User author, Item item) {
        return new Comment(null, commentDtoCreate.getText(), author, item, LocalDateTime.now());
    }

    public CommentDto commentDtoInfo(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }
}
