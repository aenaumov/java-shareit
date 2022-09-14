package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    User add(User user);

    User update(User user);

    User getOne(Long id);

    void delete(Long id);

    Collection<User> getAll();
}
