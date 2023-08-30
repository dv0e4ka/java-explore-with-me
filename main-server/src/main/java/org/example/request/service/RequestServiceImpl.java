package org.example.request.service;

import lombok.RequiredArgsConstructor;
import org.example.enums.RequestStatus;
import org.example.enums.State;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.EntityNoFoundException;
import org.example.exception.model.RequestException;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.model.ParticipationRequest;
import org.example.request.repository.RequestRepository;
import org.example.request.mapper.RequestMapper;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
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

        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
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
