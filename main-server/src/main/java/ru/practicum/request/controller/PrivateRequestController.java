package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info("получен запрос на добавления заявки пользователя id={} на участие в событии", userId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("отмена пользователя id={} на участии в событии id={}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);

    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable long userId) {
        log.info("получен запрос на получение информации заявок на участии в чужих событиях пользователя id={}", userId);
        return requestService.getUserRequests(userId);
    }
}
