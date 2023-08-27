package org.example.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.dto.*;
import org.example.event.service.EventService;
import org.example.request.dto.EventRequestStatusUpdateRequest;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventShortDto save(@Valid @RequestBody NewEventDto newEventDto, @PathVariable long userId) {
        log.info("пришел запрос на добавление события добавленным пользователем id={}", userId);
        return eventService.save(userId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchUserEvent(@Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
                               @PathVariable long userId, @PathVariable long eventId) {
        log.info("пришел запрос на изменения события id={} пользователя id={}", eventId, userId);
        return eventService.patchUserEvent(updateEventUserRequest, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult patchUserEventRequests(
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                @PathVariable long userId,
                                                @PathVariable long eventId) {
        log.info("пришел запрос на изменения статуса заявок на участии собития id={} пользователя id={}", eventId, userId);
        return requestService.patchUserEventUpdateRequests(eventRequestStatusUpdateRequest, userId, eventId);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getCurrUserEvents(@PathVariable long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("пришел запрос на получение событий добавленным пользователем id={}", userId);
        return eventService.getCurrUserEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventFull(@PathVariable long userId, @PathVariable long eventId) {
        log.info("пришел запрос на получение полной информации по событию id={} пользователя id={}", eventId, userId);
        return eventService.getEventFull(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        log.info("пришел запрос на получение информации о запросах об участии в событии id={} пользователя id={}", eventId, userId);
        return requestService.getUserEventRequests(userId, eventId);
    }
}