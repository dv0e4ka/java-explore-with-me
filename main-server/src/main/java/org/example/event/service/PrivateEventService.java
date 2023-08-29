package org.example.event.service;

import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.dto.NewEventDto;
import org.example.event.dto.UpdateEventUserRequest;
import org.example.request.dto.EventRequestStatusUpdateRequest;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;

import java.util.List;


public interface PrivateEventService {
    EventShortDto addEvent(long userId, NewEventDto newEventDto);

    EventFullDto patchUserEvent(UpdateEventUserRequest updateEvent, long userId, long eventId);

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto getUserEventFull(long userId, long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            long userId,
            long eventId);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);



}
