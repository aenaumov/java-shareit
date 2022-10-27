package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Class for integration testing ItemServiceImpl
 */
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
public class ItemServiceImplIntegrationTest {

    private final EntityManager em;
    private final ItemService service;

    @Test
    void getAllItemsByOwnerTestOk() {
        User user = new User(null, "user_1", "user_1@email.com");

        List<Item> sourceItemsList = List.of(
                new Item(null, user, "item_1", "description item_1", true, null),
                new Item(null, user, "item_2", "description item_2", true, null)
        );

        em.persist(user);

        for (Item item : sourceItemsList) {
            em.persist(item);
        }
        em.flush();

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userSaved = query.setParameter("email", user.getEmail())
                .getSingleResult();

        List<ItemDtoInfo> targetItemsList = service.getAllItemsByOwner(userSaved.getId(), 0, 10);

        assertThat(targetItemsList, hasSize(sourceItemsList.size()));

        for (Item item : sourceItemsList) {
            assertThat(targetItemsList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(item.getName())),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("lastBooking", equalTo(null)),
                    hasProperty("nextBooking", equalTo(null)),
                    hasProperty("comments", equalTo(Collections.emptyList()))
            )));

        }
    }
}