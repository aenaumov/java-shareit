package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing BookingController
 */
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private final BookingDtoCreate bookingDtoCreate =
            new BookingDtoCreate(LocalDateTime.now().plusMinutes(1L), LocalDateTime.now().plusMinutes(2L), 1L);

    private final BookingDto bookingDto =
            new BookingDto(
                    1L,
                    LocalDateTime.now().plusMinutes(1L),
                    LocalDateTime.now().plusMinutes(2L),
                    BookingStatus.APPROVED.toString(),
                    new BookingDto.ItemDto(1L, "item_1"),
                    new BookingDto.UserDto(1L)
            );

    private final List<BookingDto> bookingDtoList = List.of(bookingDto);

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void postTest() throws Exception {

        when(bookingService.add(any(BookingDtoCreate.class), eq(1L)))
                .thenReturn(bookingDto);

        ResultActions resultActions = mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andDo(print());

        resultActions.andDo(MockMvcResultHandlers.print());

        verify(bookingService, times(1))
                .add(any(BookingDtoCreate.class), eq(1L));

    }

    @Test
    void updateTest() throws Exception {

        when(bookingService.update(1L, 1L, true))
                .thenReturn(bookingDto);

        ResultActions resultActions = this.mockMvc.perform(
                        patch("/bookings/1")
                                .content(mapper.writeValueAsString(bookingDtoCreate))
                                .header("X-Sharer-User-Id", 1L)
                                .param("approved", "true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)));

        resultActions.andDo(MockMvcResultHandlers.print());

        verify(bookingService, times(1))
                .update(1L, 1L, true);

    }

    @Test
    void getBookingByIdTest() throws Exception {

        when(bookingService.getBookingById(1L, 1L))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)));

        verify(bookingService, times(1))
                .getBookingById(1L, 1L);

    }

    @Test
    void getAllUserBookingsTest() throws Exception {

        when(bookingService.getAllBookerBookings(1L, BookingState.ALL, 0, 10))
                .thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)));

        verify(bookingService, times(1))
                .getAllBookerBookings(1L, BookingState.ALL, 0, 10);

    }

    @Test
    void getAllOwnerBookingsTest() throws Exception {
        when(bookingService.getAllOwnerBookings(1L, BookingState.ALL, 0, 10))
                .thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)));

        verify(bookingService, times(1))
                .getAllOwnerBookings(1L, BookingState.ALL, 0, 10);

    }

    @Test
    void getBookingWhenBookingNotFoundExceptionTest() throws Exception {

        when(bookingService.getBookingById(1L, 1L))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404));

        verify(bookingService, times(1))
                .getBookingById(1L, 1L);
    }
}