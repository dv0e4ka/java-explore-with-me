package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.enums.EventSort;
import org.example.enums.PrivateStateAction;
import org.example.enums.RequestStatus;
import org.example.enums.State;
import org.example.event.dto.*;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.event.util.EventMapper;
import org.example.exception.model.DateTimeEventException;
import org.example.exception.model.InvalidParameterException;
import org.example.exception.model.OwnerShipConflictException;
import org.example.exception.model.PatchEventStateException;
import org.example.location.Location;
import org.example.location.LocationRepository;
import org.example.request.repository.RequestRepository;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

// TODO: сделать сервис!

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public EventShortDto save(long userId, NewEventDto newEventDto) {
        User requester = findUserById(userId);

        Category category = findCategoryById(newEventDto.getCategory());

        Location location = locationRepository.save(newEventDto.getLocation());

        Event event = EventMapper.toEvent(newEventDto, requester, location, category);
        checkDateTime(event);

        Event eventSaved = eventRepository.save(event);
        return EventMapper.toEventShortDto(eventSaved);
    }

    @Transactional
    @Override
    public EventFullDto patchUserEvent(UpdateEventUserRequest updateEventDto, long userId, long eventId) {
        User initiator = findUserById(userId);
        long initiatorId = initiator.getId();
        Event foundEvent = findEventById(eventId);

        if (initiatorId != userId) {
            throw new OwnerShipConflictException(
                    String.format("пользователь id=%d не может патчить чужое событие id=%d", userId, eventId));
        }

        if (foundEvent.getState().equals(State.PUBLISHED)) {
            throw new PatchEventStateException("изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }

        Event updateEvent = EventMapper.toEvent(updateEventDto);
        updateEvent.setInitiator(initiator);

        State state = getPrivateState(updateEventDto);
        updateEvent.setState(state);
        updateEvent.setInitiator(initiator);

        updateFields(foundEvent, updateEvent);

        Event patchedEvent = eventRepository.save(updateEvent);

        return EventMapper.toEventFullDto(patchedEvent);
    }

    private State getPrivateState(UpdateEventUserRequest updateEventUserRequest) {
        PrivateStateAction privateState = updateEventUserRequest.getStateAction();
        switch (privateState) {
            case CANCEL_REVIEW:
                return State.CANCELED;
            case SEND_TO_REVIEW:
                return State.PENDING;
            default:
                return null;
        }
    }

    private void updateLocation(Event foundEvent, Event updateEvent) {
        Location updateEventLocation = updateEvent.getLocation();
        if (updateEventLocation == null) {
            updateEvent.setLocation(foundEvent.getLocation());
        } else {
            Location foundLocation = locationRepository.findByLatAndLon(updateEventLocation.getLat(),
                    updateEventLocation.getLon());
            if (foundLocation == null) {
                Location newLocation = locationRepository.save(updateEventLocation);
                updateEvent.setLocation(newLocation);
            } else {
                updateEvent.setLocation(foundLocation);
            }
        }
    }

    private void updateCategory(Event foundEvent, Event updateEvent) {
        if (updateEvent.getCategory() == null) {
            updateEvent.setCategory(foundEvent.getCategory());
        } else {
            Category category = findCategoryById(updateEvent.getCategory().getId());
            updateEvent.setCategory(category);
        }
    }

    private void updateFields(Event foundEvent, Event updateEvent) {
        updateLocation(foundEvent, updateEvent);
        updateCategory(foundEvent, updateEvent);

        if (updateEvent.getAnnotation() == null) {
            updateEvent.setAnnotation(foundEvent.getAnnotation());
        }

        if (updateEvent.getDescription() == null) {
            updateEvent.setDescription(foundEvent.getDescription());
        }
        if (updateEvent.getEventDate() == null) {
            updateEvent.setEventDate(foundEvent.getEventDate());;
        } else {
            checkDateTime(updateEvent);
        }

        if (updateEvent.getPaid() == null) {
            updateEvent.setPaid(foundEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() == null) {
            updateEvent.setParticipantLimit(foundEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() == null) {
            updateEvent.setRequestModeration(foundEvent.getRequestModeration());
        }
        if (updateEvent.getState() == null) {
            updateEvent.setState(foundEvent.getState());
        }
        if (updateEvent.getTitle() == null) {
            updateEvent.setTitle(foundEvent.getTitle());
        }

        updateEvent.setId(foundEvent.getId());
        updateEvent.setCreatedOn(foundEvent.getCreatedOn());
        updateEvent.setPublishedOn(foundEvent.getPublishedOn());
        updateEvent.setCompilation(foundEvent.getCompilation());
    }

    private Category findCategoryById(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException(String.format("категория с id=%d не найдена", catId))
        );
    }

    private void checkDateTime(Event event) {
        LocalDateTime dateTime = event.getEventDate();
        LocalDateTime validLocalDateTime = LocalDateTime.now().plusHours(2);
        if (dateTime == null ||  dateTime.isBefore(validLocalDateTime) ) {
            throw new DateTimeEventException("Событие не удовлетворяет правилам создания, " +
                    "время создания ивента должно быть позже текущего на 2 часа");
        }
    }

    @Override
    public List<EventShortDto> getCurrUserEvents(long userId, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        findUserById(userId);
        List<Event> events = eventRepository.findByInitiator(userId, page);
        return EventMapper.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventFull(long userId, long eventId) {
        findUserById(userId);
        Event event = findEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> findEventsFull(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             int from,
                                             int size) {
        return null;
    }

    @Override
    public EventFullDto findByIdFull(long id) {
        return null;
    }

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
        if (onlyAvailable) {
            eventList = eventRepository.findAvailableEventsByPublicParameters(text,
                    categories,
                    paid,
                    rangeStartDateTime,
                    rangeEndDateTime,
                    State.PUBLISHED,
                    RequestStatus.CONFIRMED,
                    page);
            eventList.stream()
        } else {
            eventList = eventRepository.findAllEventsByPublicParameters(text,
                    categories,
                    paid,
                    rangeStartDateTime,
                    rangeEndDateTime,
                    State.PUBLISHED,
                    RequestStatus.CONFIRMED,
                    page);
        }

//        TODO:
//         информация о каждом событии должна включать в себя количество просмотров
//         и количество уже одобренных заявок на участие

//        TODO:
//         информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
//         нужно сохранить в сервисе статистики



//        только опубликованные события
        return EventMapper.toEventShortDtoList(eventList);
    }

    @Override
    public EventShortDto findByPublicIdShort(long eventId) {
        Event event = findEventById(eventId);
        return null;
    }

    @Override
    public EventShortDto patchById(long eventId, EventFullDto eventFullDto) {
        return null;
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("пользователь с id=%d не найден", userId))
        );
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("событие с id=%d не найдено", eventId))
        );
    }

    private int countConfirmedRequestByEventId(long eventId) {
        return requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}
