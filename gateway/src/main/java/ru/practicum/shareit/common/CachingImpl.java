package ru.practicum.shareit.common;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CachingImpl implements Caching {

    private final Map<String, ResponseEntity<Object>> cache = new HashMap<>();

    @Override
    public void addToCacheIfPresent(ResponseEntity<Object> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String s = Objects.requireNonNull(responseEntity.getBody()).toString();
            String myRegex = "(?<=id=)\\d+(?=.*)";
            Pattern pattern = Pattern.compile(myRegex);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String key = matcher.group();
                writeToCache(key, responseEntity);
            }
        }
    }

    @Override
    public Optional<ResponseEntity<Object>> checkInCache(String key) {
        if (cache.containsKey(key)) {
            return Optional.ofNullable(cache.get(key));
        }
        return Optional.empty();
    }

    @Override
    public void writeToCache(String key, ResponseEntity<Object> responseEntity) {
        cache.put(key, responseEntity);
    }

    @Override
    public void updateInCache(String key, ResponseEntity<Object> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            cache.replace(key, responseEntity);
        }
    }

    @Override
    public void deleteFromCache(String key) {
        Optional<ResponseEntity<Object>> data = checkInCache(key);
        if (data.isPresent()) {
            cache.remove(key);
        }
    }
}
