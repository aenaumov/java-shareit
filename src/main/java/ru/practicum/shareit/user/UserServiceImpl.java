package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExist;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto add(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        isEmailExist(user.getEmail());
        user = userRepository.add(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User user = userMapper.toUser(userDto, id);
        User storedUser = userRepository.getOne(user.getId());

        String email = user.getEmail();
        if (email != null) {
            isEmailExist(email);
            storedUser.setEmail(email);
        }

        String name = user.getName();
        if (name != null) {
            storedUser.setName(name);
        }

        userRepository.update(storedUser);
        return userMapper.toDto(storedUser);
    }

    @Override
    public UserDto getOne(Long id) {
        User user = userRepository.getOne(id);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Collection<UserDto> getAll() {
        Collection<User> users = userRepository.getAll();
        return userMapper.toUserDtoCollection(users);
    }

    /**
     * проверка уникальности email
     */
    private boolean isEmailExist(String email) {
        if (userRepository.getAll().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            throw new EmailAlreadyExist(String.format("%s email уже есть", email));
        }
        return false;
    }

}
