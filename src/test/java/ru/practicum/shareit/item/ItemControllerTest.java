package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing ItemController
 */
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private final ItemDto itemDto = new ItemDto(
            1L,
            "item_1",
            "description_item_1",
            true,
            null);
    private final List<ItemDtoInfo.CommentDto> commentDtoList = Collections.emptyList();

    private final ItemDtoInfo itemDtoInfo = new ItemDtoInfo(
            1L,
            "item_1",
            "description_item_1",
            true,
            null,
            new ItemDtoInfo.BookingDto(),
            new ItemDtoInfo.BookingDto(),
            commentDtoList);
    private final List<ItemDtoInfo> itemDtoInfoList = List.of(itemDtoInfo);
    private final List<ItemDto> itemDtoList = List.of(itemDto);

    private final CommentDtoCreate commentDtoCreate = new CommentDtoCreate("it's comment");

    private final CommentDto commentDto = new CommentDto(
            1L,
            "it's comment",
            "user_1",
            LocalDateTime.now());

    @Test
    void postItemTest() throws Exception {

        when(itemService.addItem(any(ItemDto.class), eq(1L)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        verify(itemService, times(1))
                .addItem(any(ItemDto.class), eq(1L));

    }

    @Test
    void updateItemTest() throws Exception {

        when(itemService.updateItem(any(ItemDto.class), eq(1L), eq(1L)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        verify(itemService, times(1))
                .updateItem(any(ItemDto.class), eq(1L), eq(1L));

    }

    @Test
    void getItemTest() throws Exception {

        when(itemService.getOneItem(1L, 1L))
                .thenReturn(itemDtoInfo);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoInfo.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoInfo.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoInfo.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoInfo.getAvailable())));

        verify(itemService, times(1))
                .getOneItem(1L, 1L);

    }

    @Test
    void getAllByOwnerTest() throws Exception {

        when(itemService.getAllItemsByOwner(1L, 0, 10))
                .thenReturn(itemDtoInfoList);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoInfo.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoInfo.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoInfo.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoInfo.getAvailable())));

        verify(itemService, times(1))
                .getAllItemsByOwner(1L, 0, 10);

    }

    @Test
    void deleteItemByUserTest() throws Exception {

        doNothing().when(itemService).deleteItem(1L, 1L);

        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1))
                .deleteItem(1L, 1L);
    }

    @Test
    void searchItemByTextTest() throws Exception {

        when(itemService.searchItemsByText("text", 0, 10))
                .thenReturn(itemDtoList);

        mockMvc.perform(get("/items/search")
                        .queryParam("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));

        verify(itemService, times(1))
                .searchItemsByText("text", 0, 10);

    }

    @Test
    void addCommentTest() throws Exception {

        when(itemService.addComment(eq(1L), eq(1L), any(CommentDtoCreate.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDtoCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));

        verify(itemService, times(1))
                .addComment(eq(1L), eq(1L), any(CommentDtoCreate.class));

    }

    @Test
    void postItemWhenItemNotFoundExceptionTest() throws Exception {

        when(itemService.addItem(any(ItemDto.class), eq(1L)))
                .thenThrow(ItemNotFoundException.class);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404));

        verify(itemService, times(1))
                .addItem(any(ItemDto.class), eq(1L));
    }

    @Test
    void getAllByOwnerWhenWrongParamTest() throws Exception {

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .queryParam("size", "-20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(500));
    }

}