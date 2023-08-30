package org.example.request.repository;

import org.example.enums.RequestStatus;
import org.example.event.model.Event;
import org.example.request.model.ParticipationRequest;
import org.example.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

//    PrivateRequestService. addParticipationRequest
    ParticipationRequest findByRequesterAndEvent (User user, Event event);

//    PrivateRequestService. getAllRequests
    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    Integer countByEventAndStatus(long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByEvent(Event event);
}
