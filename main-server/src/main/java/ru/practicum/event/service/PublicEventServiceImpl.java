package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.statistics.StatisticsService;
import ru.practicum.exception.model.EntityNoFoundException;
import ru.practicum.exception.model.InvalidParameterException;
import ru.practicum.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatisticsService statisticsService;


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
        saveStatistic(request);
        addViews(eventList);
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
        statisticsService.saveStats(request);
    }

    private void addViews(List<Event> events) {

        Map<Long, Long> views = statisticsService.getViews(events);
        for (Event event: events) {
            long view = views.getOrDefault(event.getId(), 0L);
            event.setViews(view);
        }
    }
}
