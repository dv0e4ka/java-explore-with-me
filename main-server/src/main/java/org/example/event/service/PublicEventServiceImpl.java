package org.example.event.service;

import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;

    @Override
    public List<EventShortDto> findEventsByPublicParameters(String text,
                                                            List<Long> categories,
                                                            Boolean paid,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            Boolean onlyAvailable,
                                                            EventSort sort,
                                                            int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    page.withSort(Sort.by(Sort.Direction.ASC, "views"));
                    break;
                default:
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

//        TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
//         нужно сохранить в сервисе статистики
        return EventMapper.toEventShortDtoList(eventList);
    }

    @Override
    public EventFullDto findByPublicIdShort(long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new EntityNoFoundException(String.format("событие с id=%d не найдено", eventId));
        } else {
            return EventMapper.toEventFullDto(event);
        }
    }
}
