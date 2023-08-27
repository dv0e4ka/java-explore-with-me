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
    public ParticipationRequestDto saveUserRequest(long userId, long eventId) {
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

        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
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
    public ParticipationRequestDto patch(long userId, long requestId) {
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


    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchUserEventUpdateRequests(
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

        if (notNeedConfirm(event)) {
            return RequestMapper.toEventRequestStatusUpdateResult(requestListOld, new ArrayList<>());
        }

//        2. нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        checkLimit(event);

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
        int currLimit = event.getParticipantLimit() - (int) event.getConfirmedRequests();

        if (currLimit == 0) {
            requestListOld.forEach(request -> request.setStatus(RequestStatus.CANCELED));
            rejectedRequests = requestListOld;
        } else {
            for (int i = 0; i < requestListOld.size(); i++) {
                if (currLimit == 0) {
                    requestListOld.get(i).setStatus(RequestStatus.CANCELED);
                    rejectedRequests.add(requestListOld.get(i));
                }
                ParticipationRequest request = requestListOld.get(i);;
                request.setStatus(requestStatusUpdate);
                confirmedRequests.add(request);
                currLimit = currLimit - 1;
            }
        }
        List<ParticipationRequest> savedConfirmedRequests =  requestRepository.saveAll(confirmedRequests);
        List<ParticipationRequest> savedRejectedRequests = requestRepository.saveAll(rejectedRequests);
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
        List<ParticipationRequest> participationRequestList = requestRepository.findAllByEvent(eventId);
        return RequestMapper.toRequestDtoList(participationRequestList);
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
}
