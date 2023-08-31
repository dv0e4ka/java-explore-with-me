package org.example.user.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.model.EntityNoFoundException;
import org.example.user.dto.NewUserRequest;
import org.example.user.dto.UserDto;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.user.mapper.UserMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto registerUser(NewUserRequest userDto) {
        User user = UserMapper.toUser(userDto);
        User userSaved = userRepository.save(user);
        return UserMapper.toUserDto(userSaved);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
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
                () -> new EntityNoFoundException("пользователь с id=" + userId + " не найден")
        );

        userRepository.deleteById(userId);
    }
}
