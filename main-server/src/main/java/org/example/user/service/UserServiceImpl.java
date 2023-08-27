package org.example.user.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.model.EntityNotFoundException;
import org.example.user.dto.UserDto;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.user.util.UserMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: заполнитоь сервис

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User userSaved = userRepository.save(user);
        return UserMapper.toUserDto(userSaved);
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids);
        } else {
            PageRequest page = PageRequest.of(from, size);
            users = userRepository.findAll(page).getContent();
        }
        return UserMapper.toUserDtoList(users);
    }

    @Transactional
    @Override
    public void delete(long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("пользователь с id=" + userId + " не найден")
        );

        userRepository.deleteById(userId);
    }
}
