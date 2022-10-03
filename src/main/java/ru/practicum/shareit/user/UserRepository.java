package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.user.model.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
}
