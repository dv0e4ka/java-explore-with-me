package org.example.app.util;

import lombok.experimental.UtilityClass;
import org.example.app.model.EndpointHit;
import org.example.app.model.ViewStats;
import org.example.dto.EndpointHitDto;
import org.example.dto.ViewStatsDto;

import java.time.LocalDateTime;

import static org.example.app.util.DataTime.formatter;

@UtilityClass
public class StatsMapper {

    public static EndpointHit toEndPointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter))
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp((endpointHit.getTimestamp().format(formatter)))
                .build();
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
