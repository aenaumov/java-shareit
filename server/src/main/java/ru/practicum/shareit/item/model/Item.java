package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.model.User;

import javax.persistence.*;

/**
 * Class Item
 */

@Entity
@Table(name = "ITEMS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_OWNER", nullable = false)
    private User owner;

    @Column(name = "ITEM_NAME")
    private String name;

    @Column(name = "ITEM_DESCRIPTION")
    private String description;

    @Column(name = "ITEM_AVAILABLE")
    private Boolean available;

    @Column(name = "REQUEST_ID")
    private Long requestId;

}
