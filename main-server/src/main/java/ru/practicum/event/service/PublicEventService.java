package org.example.event.service;

import org.example.enums.EventSort;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface PublicEventService {

    List<EventShortDto> findEventsByPublicParameters(String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     String rangeStart,
                                                     String rangeEnd,
                                                     Boolean onlyAvailable,
                                                     EventSort sort,
                                                     int from, int size,
                                                     HttpServletRequest request);

    EventFullDto findByPublicIdShort(long eventId, HttpServletRequest request);
}
