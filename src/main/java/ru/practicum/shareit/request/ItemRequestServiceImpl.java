package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.ShareItPageRequest;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInfo;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user c id %d не найден", userId)));
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, user);
        itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDtoInfo> getAllUserRequests(Long userId) {
        isUserExist(userId);
        final Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final List<ItemRequest> itemRequests = itemRequestRepository.getItemRequestsByRequestor_Id(userId, sort);
        return toItemRequestDtoInfoList(itemRequests);
    }

    @Override
    public List<ItemRequestDtoInfo> getAllRequests(Long userId, Integer from, Integer size) {
        isUserExist(userId);
        final Pageable pageable = getPageableParam(from, size);
        final List<ItemRequest> page = itemRequestRepository.getItemRequestsByRequestorIdIsNot(userId, pageable);
        return toItemRequestDtoInfoList(page);
    }

    @Override
    public ItemRequestDtoInfo getRequestById(Long userId, Long requestId) {
        isUserExist(userId);
        final ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(
                        String.format("request c id %d не найден", requestId)));
        final List<Item> listItemsByRequest = getListItemsByRequest(requestId);
        return itemRequestMapper.toItemRequestDtoInfo(itemRequest, listItemsByRequest);
    }

    private Pageable getPageableParam(Integer from, Integer size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "created");
        return new ShareItPageRequest(from, size, sort);
    }

    private List<ItemRequestDtoInfo> toItemRequestDtoInfoList(List<ItemRequest> itemRequests) {
        final List<ItemRequestDtoInfo> list = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            final List<Item> items = getListItemsByRequest(itemRequest.getId());
            list.add(itemRequestMapper.toItemRequestDtoInfo(itemRequest, items));
        }
        return list;
    }

    private List<Item> getListItemsByRequest(Long requestId) {
        return itemRepository.getItemsByRequestId(requestId);
    }

    private void isUserExist(Long idUser) {
        if (!userRepository.existsById(idUser))
            throw new UserNotFoundException(String.format("user c id %d не найден", idUser));
    }
}
