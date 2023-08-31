package ru.practicum.request.repository;

import ru.practicum.enums.RequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findByRequesterAndEvent(User user, Event event);

    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    List<ParticipationRequest> findAllByEvent(Event event);

    Integer countByEventAndStatus(Event event, RequestStatus status);


}
