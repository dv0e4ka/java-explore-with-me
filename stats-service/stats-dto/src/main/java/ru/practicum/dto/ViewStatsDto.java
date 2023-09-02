package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}
