package org.example.event.service;

import org.example.event.dto.EventFullDto;
import org.example.event.dto.UpdateEventAdminRequest;

import java.util.List;


public interface AdminEventService {

    List<EventFullDto> findEventsFull(List<Long> userIds,
                                      List<String> states,
                                      List<Long> categoryIds,
                                      String rangeStart,
                                      String rangeEnd, int from, int size);
    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest updateEvent);
}
