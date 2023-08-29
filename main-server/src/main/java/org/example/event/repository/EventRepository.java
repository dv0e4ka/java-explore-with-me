package org.example.event.repository;

import org.example.enums.RequestStatus;
import org.example.enums.State;
import org.example.event.model.Event;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIdIn(Set<Long> eventIds);

    List<Event> findByInitiator(long initiatorId, PageRequest page);

//    @Query("select distinct e " +
//            "from Event e " +
//            "join fetch e.initiator as u " +
//            "join fetch e.category as c " +
//            "left join fetch e.requests pr " +
//            "where (:text is null " +
//            "or (lower(e.annotation)) like concat('%', :text, '%') " +
//            "and (lower(e.description)) like concat('%', :text, '%')) " +
//            "and (:categories is null or e.category in :categories) " +
//            "and e.eventDate >= :rangeStart " +
//            "and e.eventDate <= :rangeEnd " +
//            "and e.paid = :paid " +
//            "and e.state = :state " +
//            "and (select count(pr) from pr " +
//                "where pr.event = e and pr.status = :status) <= e.participantLimit " +
//            "or e.participantLimit = 0")
//    List<Event> findAvailableEventsByPublicParameters(@Param("text") String text,
//                                                             @Param("categories") List<Long> categories,
//                                                             @Param("paid") boolean paid,
//                                                             @Param("rangeStart") LocalDateTime rangeStart,
//                                                             @Param("rangeEnd") LocalDateTime rangeEnd,
//                                                             @Param("state") State state,
//                                                             @Param("status") RequestStatus status,
//                                                             @Param("page") PageRequest page);

//    @Query("select distinct e " +
//            "from Event e " +
//            "join fetch e.initiator as u " +
//            "join fetch e.category as c " +
//            "left join fetch e.requests pr " +
//            "where (:text is null " +
//            "or (lower(e.annotation)) like concat('%', :text, '%') " +
//            "and (lower(e.description)) like concat('%', :text, '%')) " +
//            "and (:categories is null or e.category in :categories) " +
//            "and e.eventDate >= :rangeStart " +
//            "and e.eventDate <= :rangeEnd " +
//            "and e.paid = :paid " +
//            "and e.state = :state " +
//            "and (select count(pr) from pr " +
//            "where pr.event = e and pr.status = :status)")
//     List<Event> findAllEventsByPublicParameters(@Param("text") String text,
//                                                 @Param("categories") List<Long> categories,
//                                                 @Param("paid") boolean paid,
//                                                 @Param("rangeStart") LocalDateTime rangeStart,
//                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
//                                                 @Param("state") State state,
//                                                 @Param("status") RequestStatus status,
//                                                 @Param("page") PageRequest page);
//
//    @Query("select distinct  e " +
//            "from Event e " +
//            "left join fetch e.requests pr " +
//            "where e.state = :state " +
//            "and e.id = :eventId")
//    Event findEventWithJoinFetchByIdAndStatus(@Param("eventId") long eventId,
//                                              @Param("state") State state);
}
