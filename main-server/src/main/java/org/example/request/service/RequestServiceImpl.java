package org.example.request.service;

import lombok.RequiredArgsConstructor;
import org.example.enums.RequestStatus;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.RequestException;
import org.example.request.dto.EventRequestStatusUpdateRequest;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.model.ParticipationRequest;
import org.example.request.repository.RequestRepository;
import org.example.request.util.RequestMapper;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

        if (requestRepository.findByRequesterAndEvent(userId, eventId) != null) {
            throw new RequestException(String.format("пользователь id=%d " +
                    "не может повторно подать заявку на событие id=%d", userId, eventId));
        }

        if (event.getInitiator().getId() == userId) {
            throw new RequestException(String.format("пользователь id=%d " +
                    "не может подать заявку на собственное событие id=%d", userId, eventId));
        }

        checkLimit(event);
        int confirmedRequestsNumber = countConfirmedRequestByEventId(eventId);

        if (confirmedRequestsNumber == event.getParticipantLimit()) {
            throw new RequestException(String.format("у события id=%d достигнут лимит запросов на участие", eventId));
        }

        RequestStatus status = RequestStatus.PENDING;

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            status = RequestStatus.CONFIRMED;
        }

        ParticipationRequest request = RequestMapper.toRequest(event, user, status);

        ParticipationRequest requestSaved = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestSaved);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        findUserById(userId);
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "запрос на участи id=%d не найдено", requestId
                ))
        );
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest requestPatched = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestPatched);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        findUserById(userId);
        List<ParticipationRequest> requests = requestRepository.findAllByRequester(userId);
        return RequestMapper.toRequestDtoList(requests);
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "событие id=%d не найдено", eventId
                ))
        );
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "пользователь id=%d не найден", userId
                ))
        );
    }

    private void checkLimit(Event event) {
        int limit = event.getParticipantLimit();
        long eventId = event.getId();
        if (limit == 0) {
            return;
        }

        int eventRequests = requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
        if (eventRequests >= limit) {
            throw new RequestException(String.format("у события id=%d достигнут лимит запросов на участие", eventId));
        }
    }

    private int countConfirmedRequestByEventId(long eventId) {
        return requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}
