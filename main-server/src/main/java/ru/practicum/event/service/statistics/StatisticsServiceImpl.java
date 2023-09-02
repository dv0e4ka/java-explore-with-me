package ru.practicum.event.service.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.event.model.Event;
import ru.practicum.util.DateTimeFormat;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatsClient statsClient;
    private final ObjectMapper mapper;
    @Value(value = "${app.name}")
    private String appName;

    @Override
    public void saveStats(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .uri(path)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DateTimeFormat.formatter))
                .build();
        statsClient.add(endpointHitDto);
    }

    @Override
    public Map<Long, Long> getViews(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();

        List<Event> publishedEvent = events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .collect(Collectors.toList());

        if (events.isEmpty()) {
            return views;
        }

        Optional<LocalDateTime> minPublishedOn = publishedEvent.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (minPublishedOn.isPresent()) {
            LocalDateTime start = minPublishedOn.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = publishedEvent.stream()
                    .map(Event::getId)
                    .map(id -> ("/events/" + id))
                    .collect(Collectors.toList());

            List<ViewStatsDto> stats = getStats(start, end, uris, true);
            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri()
                        .split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }
        return views;
    }

    private List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        String startString = start.format(DateTimeFormat.formatter);
        String endString = end.format(DateTimeFormat.formatter);

        ResponseEntity<Object> response = statsClient.getStats(startString, endString, uris, unique);

        try {
            List<ViewStatsDto> s =  Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}