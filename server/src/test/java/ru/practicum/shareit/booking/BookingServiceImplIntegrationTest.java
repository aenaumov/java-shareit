package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;

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
@Sql("/testingData.sql")
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
public class BookingServiceImplIntegrationTest {

    private final BookingService service;

    @Test
    void getAllBookerBookingsTestOk() {

        List<BookingDto> targetList = service.getAllBookerBookings(2L, BookingState.ALL, 0, 10);

        assertThat(targetList, hasSize(1));

            assertThat(targetList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("item", instanceOf(BookingDto.ItemDto.class)),
                    hasProperty("item", hasProperty("id", equalTo(1L))),
                    hasProperty("item",
                            hasProperty("name", equalTo("item_1"))),
                    hasProperty("booker", instanceOf(BookingDto.UserDto.class)),
                    hasProperty("booker",
                            hasProperty("id", equalTo(2L)))
            )));
    }
}