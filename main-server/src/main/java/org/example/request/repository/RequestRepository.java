package org.example.request.repository;

import org.example.enums.RequestStatus;
import org.example.request.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester(long userId);
    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    ParticipationRequest findByRequesterAndEvent(long userId, long eventId);

    Integer countByEventAndStatus(long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByEvent(long eventId);
}
