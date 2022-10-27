package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Class for integration testing BookingServiceImpl
 */
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
public class BookingServiceImplIntegrationTest {

    private final EntityManager em;
    private final BookingService service;

    @Test
    void getAllBookerBookingsTestOk() {

        User owner = new User(null, "owner", "user_owner@email.com");
        User booker = new User(null, "booker", "user_booker@email.com");
        List<User> sourceUserList = List.of(owner, booker);

        Item item1 =
                new Item(null, owner, "item_1", "description item_1", true, null);
        Item item2 =
                new Item(null, owner, "item_2", "description item_2", true, null);
        List<Item> sourceItemsList = List.of(item1, item2);

        List<Booking> sourceBookingList = List.of(
                new Booking(null, LocalDateTime.now(), LocalDateTime.now(), item1, booker, BookingStatus.APPROVED),
                new Booking(null, LocalDateTime.now(), LocalDateTime.now(), item2, booker, BookingStatus.APPROVED)
        );

        for (User user : sourceUserList) {
            em.persist(user);
        }

        for (Item item : sourceItemsList) {
            em.persist(item);
        }

        for (Booking booking : sourceBookingList) {
            em.persist(booking);
        }
        em.flush();

        List<BookingDto> targetList = service.getAllBookerBookings(2L, BookingState.ALL, 0, 10);

        assertThat(targetList, hasSize(sourceBookingList.size()));

        for (Booking booking : sourceBookingList) {
            assertThat(targetList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("item", instanceOf(BookingDto.ItemDto.class)),
                    hasProperty("item", hasProperty("id", equalTo(booking.getItem().getId()))),
                    hasProperty("item",
                            hasProperty("name", equalTo(booking.getItem().getName()))),
                    hasProperty("booker", instanceOf(BookingDto.UserDto.class)),
                    hasProperty("booker",
                            hasProperty("id", equalTo(booking.getBooker().getId())))
            )));
        }
    }
}