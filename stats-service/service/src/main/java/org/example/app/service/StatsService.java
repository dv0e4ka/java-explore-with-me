package org.example.app.service;

import org.example.dto.EndpointHitDto;
import org.example.dto.ViewStatsDto;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    public EndpointHitDto add(EndpointHitDto endpointHitDto);

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
