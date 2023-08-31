package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.app.StatsClient;
import org.example.dto.EndpointHitDto;
import org.example.enums.EventSort;
import org.example.enums.State;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.mapper.EventMapper;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.EntityNoFoundException;
import org.example.exception.model.InvalidParameterException;
import org.example.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;


    @Transactional
    @Override
    public List<EventShortDto> findEventsByPublicParameters(String text,
                                                            List<Long> categories,
                                                            Boolean paid,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            Boolean onlyAvailable,
                                                            EventSort sort,
                                                            int from, int size,
                                                            HttpServletRequest request) {
        PageRequest page = PageRequest.of(from, size);
        if (sort != null) {
            if (sort == EventSort.VIEWS) {
                page.withSort(Sort.by(Sort.Direction.ASC, "views"));
            } else {
                page.withSort(Sort.by(Sort.Direction.ASC, "eventDate"));
            }
        }

        LocalDateTime rangeStartDateTime;
        LocalDateTime rangeEndDateTime;

        if (rangeStart != null && rangeEnd != null) {
            rangeStartDateTime = LocalDateTime.parse(rangeStart, DateTimeFormat.formatter);
            rangeEndDateTime = LocalDateTime.parse(rangeEnd, DateTimeFormat.formatter);
            if (rangeEndDateTime.isBefore(rangeStartDateTime)) {
                throw new InvalidParameterException("время завершения событие не может быть раньше начала события");
            }
        } else {
            rangeStartDateTime = LocalDateTime.now();
            rangeEndDateTime = LocalDateTime.now().plusYears(1);
        }

        List<Event> eventList = eventRepository.findAvailableEventsByPublicParameters(
                text,
                categories,
                rangeStartDateTime,
                rangeEndDateTime,
                paid,
                onlyAvailable,
                page
        );
        addViews(eventList);
        saveStatistic(request);
        return EventMapper.toEventShortDtoList(eventList);
    }

    @Override
    public EventFullDto findByPublicIdShort(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new EntityNoFoundException(String.format("событие с id=%d не найдено", eventId));
        } else {
            addViews(List.of(event));
            saveStatistic(request);
            return EventMapper.toEventFullDto(event);
        }
    }

    private void saveStatistic(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-server")
                .uri(path)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DateTimeFormat.formatter))
                .build();
        statsClient.add(endpointHitDto);
    }

    private void addViews(List<Event> events) {
        events.forEach(event -> event.setViews(+1L));
        eventRepository.saveAll(events);
    }
}
