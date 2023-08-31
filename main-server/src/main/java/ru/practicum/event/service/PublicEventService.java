package ru.practicum.event.service;

import ru.practicum.enums.EventSort;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

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
