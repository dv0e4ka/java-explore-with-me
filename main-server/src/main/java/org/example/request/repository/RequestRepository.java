package org.example.request.repository;

import org.example.request.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
}
