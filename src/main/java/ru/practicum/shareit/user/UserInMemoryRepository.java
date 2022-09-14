package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class UserInMemoryRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User add(User user) {
        final Long idUser = getNextId();
        users.put(idUser, user);
        user.setId(idUser);
        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public User getOne(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new UserNotFoundException(String.format("user c id %d не найден", id)));
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    private Long getNextId() {
        return ++id;
    }

}
