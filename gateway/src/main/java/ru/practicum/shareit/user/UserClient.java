package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.common.Caching;
import ru.practicum.shareit.common.CachingImpl;
import ru.practicum.shareit.user.dto.UserDtoCreate;

import java.util.Optional;

/**
 * Class UserClient
 */
@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    //    @Autowired
    private final Caching cache = new CachingImpl();

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addUser(UserDtoCreate userDto) {
        ResponseEntity<Object> responseEntity = post("", userDto);
        cache.addToCacheIfPresent(responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> updateUser(UserDtoCreate userDto, Long id) {
        ResponseEntity<Object> responseEntity = patch("/" + id, id, userDto);
        cache.updateInCache(String.valueOf(id), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> getOneUser(Long id) {
        Optional<ResponseEntity<Object>> data = cache.checkInCache(String.valueOf(id));
        if (data.isPresent()) {
            return data.get();
        }
        ResponseEntity<Object> responseEntity = get("/" + id, id);
        cache.writeToCache(String.valueOf(id), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        cache.deleteFromCache(String.valueOf(id));
        return delete("/" + id, id);
    }
}
