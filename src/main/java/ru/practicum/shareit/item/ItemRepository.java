package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> getItemByIdAndOwner(Long itemId, User owner);

    void deleteItemByIdAndOwner(Long itemId, User owner);

    List<Item> getAllByOwner(User owner, Pageable pageable);

    @Query(value = "SELECT i FROM Item as i " +
            "where (lower (i.description) like lower (:text) or lower (i.name) like lower (:text)) " +
            "and i.available is true ")
    List<Item> searchAvailableItemsByText(@Param("text") String text, Pageable pageable);

    List<Item> getItemsByRequestId(Long requestId);

}
