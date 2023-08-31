package ru.practicum.event.repository;

import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIdIn(List<Long> eventIds);

    List<Event> findByInitiator(User initiator, PageRequest page);

    @Query("select e " +
            "from Event e " +
            "join fetch e.initiator i " +
            "join fetch e.category c " +
            "join fetch e.location l " +
            "where e.eventDate between :start and :end " +
            "and (:userIds is null or i.id in :userIds) " +
            "and (:categoryIds is null or c.id in :categoryIds) " +
            "and (:states is null or e.state in :states) ")
    List<Event> findAllByParam(@Param("userIds") List<Long> userIds,
                               @Param("states") List<String> states,
                               @Param("categoryIds") List<Long> categoryIds,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end,
                               PageRequest page);


    @Query("select e from Event e " +
            "join fetch e.initiator as u " +
            "join fetch e.category as c " +
            "where e.state = 'PUBLISHED' " +
            "and (:text is null or (lower(e.annotation)) like lower(concat('%', :text, '%')) " +
            "or (lower(e.description)) like lower(concat('%', :text, '%'))) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and e.eventDate >= :rangeStart " +
            "and e.eventDate <= :rangeEnd " +
            "and (e.confirmedRequests < e.participantLimit or :onlyAvailable is null)"
    )
    List<Event> findAvailableEventsByPublicParameters(@Param("text") String text,
                                                      @Param("categories") List<Long> categories,
                                                      @Param("rangeStart") LocalDateTime rangeStart,
                                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                                      @Param("paid") Boolean paid,
                                                      @Param("onlyAvailable") Boolean onlyAvailable,
                                                      @Param("page") PageRequest page);

    Event findByIdAndState(long id, State state);
}
