package org.example.app.repository;

import org.example.app.model.EndpointHit;
import org.example.app.model.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new org.example.app.model.ViewStats(ep.app, ep.uri, count(ep.ip) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end);

    //todo
    @Query("select new org.example.app.model.ViewStats(ep.app, ep.uri, COUNT(DISTINCT(ep.ip)) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> findViewStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new org.example.app.model.ViewStats(ep.app, ep.uri, COUNT(ep.ip) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "AND ep.uri in ?3 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> viewStatsListByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    //todo
    @Query("select new org.example.app.model.ViewStats(ep.app, ep.uri, COUNT(DISTINCT(ep.ip)) as ips) " +
            "from EndpointHit as ep " +
            "where ep.timestamp BETWEEN ?1 and ?2 " +
            "AND ep.uri in ?3 " +
            "group by ep.app, ep.uri " +
            "ORDER BY ips DESC")
    public List<ViewStats> viewStatsListByUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
