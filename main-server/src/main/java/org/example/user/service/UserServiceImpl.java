package org.example.user.service;

import lombok.RequiredArgsConstructor;
import org.example.user.dto.UserDto;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.user.util.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: заполнитоь сервис

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User userSaved = userRepository.save(user);
        return UserMapper.toUserDto(userSaved);
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, int size, int from) {
        return null;
    }

    @Override
    public void delete(long userId) {

    }
}
