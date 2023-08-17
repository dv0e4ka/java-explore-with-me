package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ViewStatsDto {
    String app;
    String uri;
    long hits;
}
