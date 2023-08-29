package org.example.user.service;

import org.example.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int size, int from);

    UserDto registerUser(UserDto userDto);

    void delete(long userId);
}
