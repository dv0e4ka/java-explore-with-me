package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;


public interface PrivateEventService {
    EventFullDto addEvent(long userId, NewEventDto newEventDto);

    EventFullDto patchUserEvent(UpdateEventUserRequest updateEvent, long userId, long eventId);

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto getUserEventFull(long userId, long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            long userId,
            long eventId);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);




}
