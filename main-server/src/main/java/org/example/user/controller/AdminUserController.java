package org.example.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.dto.UserDto;
import org.example.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll(@RequestParam (required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") @PositiveOrZero int size,
                                 @RequestParam(defaultValue = "10") @Positive int from) {
        log.info("получен запрос на получение информации о пользователях");
        return userService.findAll(ids, size, from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid UserDto userDto) {
        log.info("получен запрос на добавление нового пользователя: {}", userDto.getName());
        return userService.save(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("получен запрос на удаления пользователя id={}", userId);
        userService.delete(userId);
    }
}
