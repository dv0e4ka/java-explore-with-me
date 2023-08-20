package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.util.StatsMapper;
import org.example.app.model.EndpointHit;
import org.example.app.model.ViewStats;
import org.example.app.repository.StatsRepository;
import org.example.dto.EndpointHitDto;
import org.example.dto.ViewStatsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    public EndpointHitDto add(EndpointHitDto endpointHitDto) {
        EndpointHit toSave = StatsMapper.toEndPointHit(endpointHitDto);
        EndpointHit endpointHitSaved = statsRepository.save(toSave);

        return StatsMapper.toEndpointHitDto(endpointHitSaved);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> viewStatsList;
        if (uris == null) {
            if (unique) {
                viewStatsList = statsRepository.findViewStatsUnique(start, end);;
            } else {
                viewStatsList = statsRepository.findViewStats(start, end);
            }
        } else {
            if (unique) {
                viewStatsList = statsRepository.viewStatsListByUrisUnique(start, end, uris);
            } else {
                viewStatsList = statsRepository.viewStatsListByUris(start, end, uris);
            }
        }
        return viewStatsList.stream().map(StatsMapper::toViewStatsDto).collect(Collectors.toList());
    }
}

