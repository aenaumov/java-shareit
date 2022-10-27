package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RepositoryRestResource(path = "items")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByItem(Item item);

}
