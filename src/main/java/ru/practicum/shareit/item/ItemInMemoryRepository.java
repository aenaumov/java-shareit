package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Repository
@AllArgsConstructor
@NoArgsConstructor
public class ItemInMemoryRepository implements ItemRepository {

    private Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item add(Item item) {
        final Long id = getNextId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        final Long id = item.getId();
        items.replace(id, item);
        return items.get(id);
    }

    @Override
    public Item getOne(Long itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new ItemNotFoundException(String.format("item c id %d не найден", id)));
    }

    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }

    private Long getNextId() {
        return ++id;
    }
}
