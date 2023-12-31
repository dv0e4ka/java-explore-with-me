package ru.practicum.app.repository;

import ru.practicum.app.model.EndpointHit;
import ru.practicum.app.model.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.app.model.ViewStats(ep.app, ep.uri, count(ep.ip) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.app.model.ViewStats(ep.app, ep.uri, COUNT(DISTINCT(ep.ip)) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> findViewStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.app.model.ViewStats(ep.app, ep.uri, COUNT(ep.ip) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "AND ep.uri in ?3 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> viewStatsListByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.app.model.ViewStats(ep.app, ep.uri, COUNT(DISTINCT(ep.ip)) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "AND ep.uri in ?3 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> viewStatsListByUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
