package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.enums.EventSort;
import org.example.enums.RequestStatus;
import org.example.enums.State;
import org.example.event.dto.EventShortDto;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.event.util.EventMapper;
import org.example.exception.model.InvalidParameterException;
import org.example.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        switch (sort) {
            case VIEWS:
//                page.withSort(Sort.by("views").descending());
                page.withSort(Sort.by(Sort.Direction.ASC, "views"));
                break;
            default:
//                page.withSort(Sort.by("eventDate").ascending());
                page.withSort(Sort.by(Sort.Direction.ASC, "eventDate"));

        }

//        текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв


//        если в запросе не указан диапазон дат [rangeStart-rangeEnd],
//        то нужно выгружать события, которые произойдут позже текущей даты и времени

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
            rangeEndDateTime = LocalDateTime.now().plusMonths(1);
        }

        if (text != null) {
            text = text.toLowerCase();
        }

        List<Event> eventList;
//        if (onlyAvailable) {
//            eventList = eventRepository.findAvailableEventsByPublicParameters(text,
//                    categories,
//                    paid,
//                    rangeStartDateTime,
//                    rangeEndDateTime,
//                    State.PUBLISHED,
//                    RequestStatus.CONFIRMED,
//                    page);
////            eventList.stream()
//        } else {
//            eventList = eventRepository.findAllEventsByPublicParameters(text,
//                    categories,
//                    paid,
//                    rangeStartDateTime,
//                    rangeEndDateTime,
//                    State.PUBLISHED,
//                    RequestStatus.CONFIRMED,
//                    page);
//        }

//        TODO:
//         информация о каждом событии должна включать в себя количество просмотров
//         и количество уже одобренных заявок на участие

//        TODO:
//         информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
//         нужно сохранить в сервисе статистики



//        только опубликованные события
//        return EventMapper.toEventShortDtoList(eventList);
        return null;
    }

    @Override
    public EventShortDto findByPublicIdShort(long eventId) {
        Event event = findEventById(eventId);
        return null;
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("событие с id=%d не найдено", eventId))
        );
    }
}
