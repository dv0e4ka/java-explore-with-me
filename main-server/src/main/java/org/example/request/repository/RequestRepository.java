package org.example.request.repository;

import org.example.enums.RequestStatus;
import org.example.event.model.Event;
import org.example.request.model.ParticipationRequest;
import org.example.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findByRequesterAndEvent (User user, Event event);

    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    List<ParticipationRequest> findAllByEvent(Event event);

    List<ParticipationRequest> findAllByEventAndStatus(Event event, RequestStatus status);

    List<ParticipationRequest> findAllByStatus(RequestStatus status);

    Integer countByEventAndStatus(Event event, RequestStatus status);


}
