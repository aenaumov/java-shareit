package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class ItemRequest
 */

@Entity
@Table(name = "REQUESTS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REQUESTOR_ID", nullable = false)
    private User requestor;

    @Column(name = "CREATED")
    private LocalDateTime created;

}
