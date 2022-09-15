package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotOwnerOfItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ItemDto add(ItemDto itemDto, Long idUser) {
        final Item item = itemMapper.toItem(itemDto, null, idUser);
        userRepository.getOne(item.getIdOwner());
        Item storedItem = itemRepository.add(item);
        return itemMapper.toDto(storedItem);

    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long idUser) {
        final Item item = itemMapper.toItem(itemDto, itemId, idUser);
        userRepository.getOne(idUser);
        checkUserAndItemCorresponding(itemId, idUser);
        Item storedItem = itemRepository.getOne(itemId);

        String name = item.getName();
        if (name != null) {
            if (!name.isBlank()) {
                storedItem.setName(name);
            }
        }

        String description = item.getDescription();
        if (description != null) {
            if (!description.isBlank()) {
                storedItem.setDescription(description);
            }
        }

        Boolean available = item.getAvailable();
        if (available != null) {
            storedItem.setAvailable(available);
        }
        itemRepository.update(storedItem);
        return itemMapper.toDto(storedItem);
    }

    @Override
    public ItemDto getOne(Long itemId) {
        Item item = itemRepository.getOne(itemId);
        return itemMapper.toDto(item);
    }

    @Override
    public void delete(Long itemId, Long userId) {
        userRepository.getOne(userId);
        checkUserAndItemCorresponding(itemId, userId);
        itemRepository.delete(itemId);

    }

    @Override
    public Collection<ItemDto> getAllByOwner(Long userId) {
        userRepository.getOne(userId);
        Collection<Item> items = itemRepository.getAll().stream()
                .filter(item -> item.getIdOwner().equals(userId))
                .collect(Collectors.toList());
        return itemMapper.toItemDtoCollection(items);
    }

    @Override
    public Collection<ItemDto> searchByText(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        String myRegex = ".*" + text + ".*";
        Pattern pattern = Pattern.compile(myRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Predicate<String> str = pattern.asPredicate();

        Collection<Item> items = itemRepository.getAll().stream()
                .filter(item -> str.test(item.getName()) || str.test(item.getDescription()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return itemMapper.toItemDtoCollection(items);
    }

    /**
     * проверка user хозяин item
     */
    private void checkUserAndItemCorresponding(Long itemId, Long userId) {
        Item storedItem = itemRepository.getOne(itemId);
        if (!storedItem.getIdOwner().equals(userId)) {
            throw new UserNotOwnerOfItemException(String.format("item c id %d не принадлежит user c id %d",
                    itemId, userId));
        }
    }

}
