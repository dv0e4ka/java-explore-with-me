package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.util.List;


public interface AdminEventService {

    List<EventFullDto> findEventsFull(List<Long> userIds,
                                      List<String> states,
                                      List<Long> categoryIds,
                                      String rangeStart,
                                      String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest updateEvent);
}
