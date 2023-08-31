package org.example.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.enums.PrivateStateAction;
import org.example.enums.RequestStatus;
import org.example.enums.State;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.dto.NewEventDto;
import org.example.event.dto.UpdateEventUserRequest;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.event.mapper.EventMapper;
import org.example.exception.model.DateTimeEventException;
import org.example.exception.model.OwnerShipConflictException;
import org.example.exception.model.PatchEventStateException;
import org.example.exception.model.RequestException;
import org.example.location.Location;
import org.example.location.LocationRepository;
import org.example.request.dto.EventRequestStatusUpdateRequest;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.model.ParticipationRequest;
import org.example.request.repository.RequestRepository;
import org.example.request.mapper.RequestMapper;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        User requester = findUserById(userId);

        Category category = findCategoryById(newEventDto.getCategory());

        Location location = locationRepository.save(newEventDto.getLocation());

        Event event = EventMapper.toEvent(newEventDto, requester, location, category);
        checkDateTime(event);

        log.info("пришол в private ивент title={}", newEventDto.getTitle());
        log.info("пришол в private описание ивента title={}", newEventDto.getDescription());

        Event eventSaved = eventRepository.save(event);
        log.info("добавлен в private ивент id={}", eventSaved.getId());
        log.info("добавлен в private ивент title={}", eventSaved.getTitle());
        log.info("добавлено в private описание ивента title={}", eventSaved.getDescription());
        return EventMapper.toEventFullDto(eventSaved);
    }

    @Transactional
    @Override
    public EventFullDto patchUserEvent(UpdateEventUserRequest updateEvent, long userId, long eventId) {
        User user = findUserById(userId);
        Event eventFound = findEventById(eventId);

        if (eventFound.getInitiator().getId() != userId) {
            throw new OwnerShipConflictException(
                    String.format("пользователь id=%d не может патчить чужое событие id=%d", userId, eventId));
        }

        if (eventFound.getState().equals(State.PUBLISHED)) {
            throw new PatchEventStateException("изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }

        Event updatedFieldsEvent = updateFieldByUserRequest(eventFound, updateEvent);
        Event patchedEvent = eventRepository.save(updatedFieldsEvent);
        return EventMapper.toEventFullDto(patchedEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        User user = findUserById(userId);
        List<Event> events = eventRepository.findByInitiator(user, page);
        return EventMapper.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getUserEventFull(long userId, long eventId) {
        findUserById(userId);
        Event event = findEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, long userId, long eventId) {
        // инициализация
        findUserById(userId);
        Event event = findEventById(eventId);
        List<ParticipationRequest> requestListOld = requestRepository.findAllByIdIn(
                eventRequestStatusUpdateRequest.getRequestIds());

//        аутентификация
//        0. отношение id заявок к id событию
        boolean isAccording = requestListOld.stream().allMatch(request -> request.getEvent().getId() == eventId);
        if (!isAccording) {
            throw new RequestException(
                    String.format("айди заявок должны относиться к событие id=%d", eventId));
        }

//        1. если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
// TODO добавить конферм
        if (notNeedConfirm(event)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + eventRequestStatusUpdateRequest.getRequestIds().size());
            eventRepository.save(event);
            return RequestMapper.toEventRequestStatusUpdateResult(requestListOld, new ArrayList<>());
        }

//        2. нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
         int confirmedRequestNumber = countLimit(event);

//        3. статус можно изменить только у заявок, находящихся в состоянии ожидания
        boolean isPending = requestListOld.stream().allMatch(request -> request.getStatus() == RequestStatus.PENDING);
        if (!isPending) {
            throw new RequestException("статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }

//        4. если при подтверждении данной заявки, лимит заявок для события исчерпан,
//        то все неподтверждённые заявки необходимо отклонить

        RequestStatus requestStatusUpdate = eventRequestStatusUpdateRequest.getStatus();
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        if (requestStatusUpdate.equals(RequestStatus.REJECTED)) {
            requestListOld.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            return RequestMapper.toEventRequestStatusUpdateResult(new ArrayList<>(), requestListOld);
        }
        int currLimit = event.getParticipantLimit() - confirmedRequestNumber;

        int additionalConfirmRequests = 0;
        if (currLimit == 0) {
            requestListOld.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            rejectedRequests = requestListOld;
        } else {
            for (int i = 0; i < requestListOld.size(); i++) {
                if (currLimit == 0) {
                    requestListOld.get(i).setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(requestListOld.get(i));
                }
                ParticipationRequest request = requestListOld.get(i);
                request.setStatus(requestStatusUpdate);
                if (requestStatusUpdate.equals(RequestStatus.CONFIRMED)) {
                    additionalConfirmRequests++;
                }
                confirmedRequests.add(request);
                currLimit = currLimit - 1;
            }
        }
        List<ParticipationRequest> savedConfirmedRequests =  requestRepository.saveAll(confirmedRequests);
        List<ParticipationRequest> savedRejectedRequests = requestRepository.saveAll(rejectedRequests);
        event.setConfirmedRequests(event.getConfirmedRequests() + additionalConfirmRequests);
        eventRepository.save(event);
        return RequestMapper.toEventRequestStatusUpdateResult(savedConfirmedRequests, savedRejectedRequests);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId) {
        findUserById(userId);
        Event event = findEventById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new RequestException(String.format(
                    "пользователь id=%d не может просматривать заявки к чужому событию id=%d", userId, eventId
            ));
        }
        List<ParticipationRequest> participationRequestList = requestRepository.findAllByEvent(event);
        return RequestMapper.toRequestDtoList(participationRequestList);
    }

    private Event updateFieldByUserRequest(Event event, UpdateEventUserRequest updateRequest) {
        String annotation = updateRequest.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }

        Long categoryId = updateRequest.getCategory();
        if (categoryId != null) {
            Category category = findCategoryById(categoryId);
            event.setCategory(category);
        }

        String description = updateRequest.getDescription();
        if (description != null) {
            event.setDescription(description);
        }

        String eventDateString = updateRequest.getEventDate();
        if (eventDateString != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventDateString, DateTimeFormat.formatter);
            event.setEventDate(eventDate);
            checkDateTime(event);
        }

        Location updateLocation = updateRequest.getLocation();
        if (updateLocation != null) {
            float lat = updateLocation.getLat();
            float lon = updateLocation.getLon();

            Location location = locationRepository.findByLatAndLon(lat, lon)
                    .stream()
                    .findAny()
                    .orElse(locationRepository.save(
                                    new Location(lat, lon)
                            )
                    );
            event.setLocation(location);
        }

        Boolean paid = updateRequest.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }

        Integer participantLimit = updateRequest.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        Boolean requestModeration = updateRequest.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        PrivateStateAction privateState = updateRequest.getStateAction();
        if (privateState != null) {
            State state = getPrivateState(updateRequest);
            event.setState(state);
        }

        String title = updateRequest.getTitle();
        if (title != null) {
            event.setTitle(title);
        }

        return event;
    }

    private Integer countLimit(Event event) {
        long eventId = event.getId();

        int limit = event.getParticipantLimit();
        if (limit == 0) {
            return 0;
        }

        int eventRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (eventRequests >= limit) {
            throw new RequestException(String.format("у события id=%d достигнут лимит запросов на участие", eventId));
        } else {
            return eventRequests;
        }
    }

    private boolean notNeedConfirm(Event event) {
        int limit = event.getParticipantLimit();
        if (limit == 0) {
            return true;
        }

        if (!event.getRequestModeration()) {
            return true;
        }
        return false;
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

    private void checkDateTime(Event event) {
        LocalDateTime dateTime = event.getEventDate();
        LocalDateTime validLocalDateTime = LocalDateTime.now().plusHours(2);
        if (dateTime == null ||  dateTime.isBefore(validLocalDateTime) ) {
            throw new DateTimeEventException("Событие не удовлетворяет правилам создания, " +
                    "время создания ивента должно быть позже текущего на 2 часа");
        }
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("пользователь с id=%d не найден", userId))
        );
    }

    private Category findCategoryById(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException(String.format("категория с id=%d не найдена", catId))
        );
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("событие с id=%d не найдено", eventId))
        );
    }
}
