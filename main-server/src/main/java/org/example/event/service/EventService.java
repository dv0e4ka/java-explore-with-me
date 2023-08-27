package org.example.event.service;

import org.example.event.dto.*;

import java.util.List;


public interface EventService {
    public EventShortDto save(long userId, NewEventDto newEventDto);

    public EventFullDto patchUserEvent(UpdateEventUserRequest updateEvent, long userId, long eventId);

    public List<EventShortDto> getCurrUserEvents(long userId, int from, int size);

    public EventFullDto getEventFull(long userId, long eventId);

    public List<EventFullDto> findEventsFull(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd, int from, int size);
    public EventFullDto findByIdFull(long id);

    public List<EventShortDto> findEventsShort(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd, int from, int size);
    public EventShortDto patchById(long eventId, EventFullDto eventFullDto);
}
