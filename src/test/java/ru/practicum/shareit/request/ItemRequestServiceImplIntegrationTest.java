package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Class for integration testing ItemRequestServiceImpl
 */
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
public class ItemRequestServiceImplIntegrationTest {

    private final EntityManager em;
    private final ItemRequestService service;


    @Test
    void getAllRequestsTestOk() {

        User user = new User(null, "user_1", "user_1@email.com");
        User user2 = new User(null, "user_2", "user_2@email.com");

        List<ItemRequest> sourceList = List.of(
                new ItemRequest(null, "wanna phone", user, LocalDateTime.now()),
                new ItemRequest(null, "wanna gasket", user, LocalDateTime.now())
        );

        em.persist(user);
        for (ItemRequest itemRequest : sourceList) {
            em.persist(itemRequest);
        }
        em.persist(user2);
        em.flush();

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userSaved = query.setParameter("email", user2.getEmail())
                .getSingleResult();

        List<ItemRequestDtoInfo> targetList = service.getAllRequests(userSaved.getId(), 0,10);

        assertThat(targetList, hasSize(sourceList.size()));

        for (ItemRequest item : sourceList) {
            assertThat(targetList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("items", equalTo(Collections.emptyList()))
            )));
        }
    }

}
