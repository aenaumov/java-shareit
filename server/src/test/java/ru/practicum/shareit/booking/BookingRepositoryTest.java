package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Class for testing BookingRepository
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("/testingData.sql")
@AutoConfigureTestDatabase
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void getCurrentBookingsForBookerTest() {

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("SELECT b FROM Booking as b " +
                                "where b.booker.id = :userId " +
                                "AND b.start < :time " +
                                "AND b.end > :time",
                        Booking.class);

        List<Booking> list = query
                .setParameter("userId", 2L)
                .setParameter("time", LocalDateTime.now())
                .getResultList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item_1", list.get(0).getItem().getName());

    }

    @Test
    void getCurrentBookingsForOwnerTest() {

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("SELECT b FROM Booking as b " +
                                "where b.item.owner.id = :userId " +
                                "AND b.start < :time " +
                                "AND b.end > :time",
                        Booking.class);
        List<Booking> list = query
                .setParameter("userId", 1L)
                .setParameter("time", LocalDateTime.now())
                .getResultList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item_1", list.get(0).getItem().getName());
    }

    @Test
    void findApprovedBookingsInPastByUserTest() {

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("SELECT b FROM Booking as b " +
                                "where b.booker.id = :bookerId " +
                                "AND b.item.id = :itemId " +
                                "AND b.status = 'APPROVED' " +
                                "AND b.end < :timeNow",
                        Booking.class);
        List<Booking> list = query
                .setParameter("bookerId", 3L)
                .setParameter("itemId", 1L)
                .setParameter("timeNow", LocalDateTime.now())
                .getResultList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item_1", list.get(0).getItem().getName());
    }

    @Test
    void getBookingsByItemIdAndEndTimeIsAfterTest() {

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("SELECT b FROM Booking as b " +
                                "where b.item.id = :itemId " +
                                "AND b.status = 'APPROVED' " +
                                "AND b.end >= :startTime",
                        Booking.class);
        List<Booking> list = query
                .setParameter("itemId", 1L)
                .setParameter("startTime", LocalDateTime.now())
                .getResultList();
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("item_1", list.get(0).getItem().getName());
        Assertions.assertEquals(2L, list.get(0).getBooker().getId());
        Assertions.assertEquals(3L, list.get(1).getBooker().getId());
    }
}
