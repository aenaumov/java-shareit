package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Class for testing ItemRepository
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("/testingData.sql")
@AutoConfigureTestDatabase
@TestPropertySource(
        locations =
                "classpath:application-test.properties"
)
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void searchAvailableItemsByText() {

        TypedQuery<Item> query = em.getEntityManager()
                .createQuery("SELECT i FROM Item as i " +
                                "where (lower (i.description) like lower (:text) " +
                                "or lower (i.name) like lower (:text)) " +
                                "and i.available is true",
                        Item.class);
        List<Item> list = query.setParameter("text", "%fInDmE%").getResultList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("tryToFindMe", list.get(0).getDescription());

    }
}