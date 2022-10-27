package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoCreate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class UserClient
 */
@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    private final Map<String, ResponseEntity<Object>> book = new HashMap<>();

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
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(UserDtoCreate userDto, Long id) {
        deleteFromBook(String.valueOf(id));
        return patch("/" + id, id, userDto);
    }

    public ResponseEntity<Object> getOneUser(Long id) {
        Optional<ResponseEntity<Object>> data = checkInBook(String.valueOf(id));
        if (data.isPresent()) {
            return data.get();
        }
        ResponseEntity<Object> responseEntity = get("/" + id, id);
        writeToBook(String.valueOf(id), responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        deleteFromBook(String.valueOf(id));
        return delete("/" + id, id);
    }

    private Optional<ResponseEntity<Object>> checkInBook(String key) {
        if (book.containsKey(key)) {
            return Optional.ofNullable(book.get(key));
        }
        return Optional.empty();
    }

    private void writeToBook(String key, ResponseEntity<Object> responseEntity) {
        book.put(key, responseEntity);
    }

    private void deleteFromBook(String key) {
        Optional<ResponseEntity<Object>> data = checkInBook(key);
        if (data.isPresent()) {
            book.remove(key);
        }
    }

}
