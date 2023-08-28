package org.example.event.service;

import org.example.enums.EventSort;
import org.example.event.dto.*;

import java.util.List;


public interface EventService {
    EventShortDto save(long userId, NewEventDto newEventDto);

    EventFullDto patchUserEvent(UpdateEventUserRequest updateEvent, long userId, long eventId);

    List<EventShortDto> getCurrUserEvents(long userId, int from, int size);

    EventFullDto getEventFull(long userId, long eventId);

    List<EventFullDto> findEventsFull(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd, int from, int size);
    EventFullDto findByIdFull(long id);

    List<EventShortDto> findEventsByPublicParameters(String text,
                                                            List<Long> categories,
                                                            Boolean paid,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            Boolean onlyAvailable,
                                                            EventSort sort,
                                                            int from, int size);
    EventShortDto patchById(long eventId, EventFullDto eventFullDto);

    EventShortDto findByPublicIdShort(long eventId);
}
