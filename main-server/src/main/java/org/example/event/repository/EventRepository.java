package org.example.event.repository;

import org.example.event.model.Event;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    public List<Event> findByIdIn(Set<Long> eventIds);

    public List<Event> findByInitiator(long initiatorId, PageRequest page);
}
