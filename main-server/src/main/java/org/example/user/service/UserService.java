package org.example.user.service;

import org.example.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll(List<Long> ids, int size, int from);

    UserDto save(UserDto userDto);

    void delete(long userId);
}
