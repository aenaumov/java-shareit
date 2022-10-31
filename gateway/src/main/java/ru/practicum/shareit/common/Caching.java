package ru.practicum.shareit.common;


import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
*Interface for management Caching
*/
public interface Caching {

    /**
     * добавить в кэш, если объект есть
     */
    void addToCacheIfPresent(ResponseEntity<Object> responseEntity);

    /**
     * проверить наличие в кэше
     */
    Optional<ResponseEntity<Object>> checkInCache(String key);

    /**
     * добавить в кэш
     */
    void writeToCache(String key, ResponseEntity<Object> responseEntity);

    /**
     * обновить кэш
     */
    void updateInCache(String key, ResponseEntity<Object> responseEntity);

    /**
     * удалить из кэша
     */
    void deleteFromCache(String key);
}
