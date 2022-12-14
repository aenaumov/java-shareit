package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.ShareItPageRequest;
import ru.practicum.shareit.exception.AuthorNotBookerOfItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, Long idUser) {
        final User user = getUserById(idUser);
        final Item item = itemRepository.save(itemMapper.toItem(itemDto, null, user));
        return itemMapper.toDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long idUser) {
        final User user = getUserById(idUser);
        Item item = itemRepository.getItemByIdAndOwner(itemId, user)
                .orElseThrow(() -> new NotFoundException(String.format("item c id %d ???? ????????????", itemId)));
        Optional.ofNullable(itemDto.getName()).filter(s -> !s.isBlank()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).filter(s -> !s.isBlank()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        return itemMapper.toDto(item);
    }

    @Override
    public ItemDtoInfo getOneItem(Long itemId, Long userId) {
        final Item item = getItemById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = getLastBooking(item.getId());
            Booking nextBooking = getNextBooking(item.getId());
            List<Comment> comments = getCommentsByItem(item);
            return itemMapper.toItemDtoInfo(item, lastBooking, nextBooking, comments);
        }
        List<Comment> comments = getCommentsByItem(item);
        return itemMapper.toItemDtoInfo(item, null, null, comments);
    }

    @Transactional
    @Override
    public void deleteItem(Long itemId, Long userId) {
        final User owner = getUserById(userId);
        itemRepository.deleteItemByIdAndOwner(itemId, owner);
    }

    @Override
    public List<ItemDtoInfo> getAllItemsByOwner(Long userId, Integer from, Integer size) {
        final User owner = getUserById(userId);
        final Pageable pageable = new ShareItPageRequest(from, size, Sort.unsorted());
        final List<Item> page = itemRepository.getAllByOwner(owner, pageable);
        return toItemDtoInfoList(page);
    }

    @Override
    public List<ItemDto> searchItemsByText(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        text = "%" + text + "%";
        final Pageable pageable = new ShareItPageRequest(from, size, Sort.unsorted());
        final List<Item> page = itemRepository.searchAvailableItemsByText(text, pageable);
        return itemMapper.toItemDtoList(page);
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDtoCreate commentDtoCreate) {
        User author = getUserById(userId);
        Item item = getItemById(itemId);
        if (!isUserBookedItem(author, item)) {
            throw new AuthorNotBookerOfItemException(
                    String.format("user id %d isn't booker of item id %d", userId, itemId));
        }
        Comment comment = commentMapper.toComment(commentDtoCreate, author, item);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    private Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("item c id %d ???? ????????????", id)));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("user c id %d ???? ????????????", id)));
    }

    private boolean isUserBookedItem(User booker, Item item) {
        final LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findApprovedBookingsInPastByUser(booker.getId(), item.getId(), time).size() > 0;

    }

    private Booking getLastBooking(Long itemId) {
        final Sort sortLast = Sort.by(Sort.Direction.DESC, "start");
        final LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findFirstByItem_IdAndStartIsBefore(itemId, time, sortLast).orElse(null);
    }

    private Booking getNextBooking(Long itemId) {
        final Sort sortNext = Sort.by(Sort.Direction.ASC, "start");
        final LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findFirstByItem_IdAndStartIsAfter(itemId, time, sortNext).orElse(null);
    }

    private List<Comment> getCommentsByItem(Item item) {
        return commentRepository.findCommentsByItem(item);
    }

    private List<ItemDtoInfo> toItemDtoInfoList(List<Item> items) {
        final List<ItemDtoInfo> itemsDtoInfo = new ArrayList<>();
        for (Item item : items) {
            Booking lastBooking = getLastBooking(item.getId());
            Booking nextBooking = getNextBooking(item.getId());
            List<Comment> comments = getCommentsByItem(item);
            itemsDtoInfo.add(itemMapper.toItemDtoInfo(item, lastBooking, nextBooking, comments));
        }
        return itemsDtoInfo;
    }

}
