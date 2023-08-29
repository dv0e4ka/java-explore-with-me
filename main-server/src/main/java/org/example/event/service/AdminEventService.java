package org.example.event.service;

import org.example.enums.EventSort;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.dto.NewEventDto;
import org.example.event.dto.UpdateEventUserRequest;

import java.util.List;


public interface AdminEventService {

    List<EventFullDto> findEventsFull(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd, int from, int size);
    EventFullDto findByIdFull(long id);
}
