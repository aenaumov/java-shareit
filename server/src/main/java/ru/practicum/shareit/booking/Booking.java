package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class Booking
 */

@Entity
@Table(name = "BOOKINGS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long id;

    @Column(name = "START_BOOKING")
    private LocalDateTime start;

    @Column(name = "END_BOOKING")
    private LocalDateTime end;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User booker;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
