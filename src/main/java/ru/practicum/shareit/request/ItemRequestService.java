package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;

import java.util.List;

/**
 * Интерфейс для работы с ItemRequest
 */
public interface ItemRequestService {

    /**
     * создать request
     */
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long idUser);

    /**
     * получить список запросов пользователя
     */
    List<ItemRequestDtoInfo> getAllUserRequests(Long idUser);

    /**
     * получить список всех запросов
     */
    List<ItemRequestDtoInfo> getAllRequests(Long idUser, Integer from, Integer size);

    /**
     * получить запрос по id
     */
    ItemRequestDtoInfo getRequestById(Long idUser, Long requestId);
}
