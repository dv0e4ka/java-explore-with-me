package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.enums.RequestStatus;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.EntityNoFoundException;
import ru.practicum.exception.model.RequestException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new RequestException(String.format("пользователь id=%d " +
                    "не может подать заявку на собственное событие id=%d", userId, eventId));
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestException(String.format("нельзя участвовать в неопубликованном событии id=%d", eventId));
        }

        if (requestRepository.findByRequesterAndEvent(user, event) != null) {
            throw new RequestException(String.format("пользователь id=%d " +
                    "не может повторно подать заявку на событие id=%d", userId, eventId));
        }

        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new RequestException(String.format("у события id=%d достигнут лимит запросов на участие", eventId));
        }

        RequestStatus status = RequestStatus.PENDING;

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            status = RequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        ParticipationRequest request = RequestMapper.toRequest(event, user, status);

        ParticipationRequest requestSaved = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestSaved);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        User user = findUserById(userId);
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNoFoundException(String.format(
                        "запрос на участи id=%d не найдено", requestId
                ))
        );
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest requestCanceled = requestRepository.save(request);
        // TODO: убедиться что userId, eventId войдут в дто
        return RequestMapper.toRequestDto(requestCanceled);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        User user = findUserById(userId);
        List<ParticipationRequest> requests = requestRepository.findAllByRequester(user);
        return RequestMapper.toRequestDtoList(requests);
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNoFoundException(String.format(
                        "событие id=%d не найдено", eventId
                ))
        );
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNoFoundException(String.format(
                        "пользователь id=%d не найден", userId
                ))
        );
    }
}
