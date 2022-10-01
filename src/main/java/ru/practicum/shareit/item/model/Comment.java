package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class Comment
 */

@Entity
@Table(name = "COMMENTS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    Long id;

    @Column(name = "COMMENT_TEXT")
    String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = false)
    User author;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    Item item;

    @Column(name = "CREATED")
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
