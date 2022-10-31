package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class ItemRequestDtoInfo
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoInfo {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }
}