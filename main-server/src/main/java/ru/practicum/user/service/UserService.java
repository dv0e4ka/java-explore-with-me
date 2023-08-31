package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int size, int from);

    UserDto registerUser(NewUserRequest userDto);

    void delete(long userId);
}
