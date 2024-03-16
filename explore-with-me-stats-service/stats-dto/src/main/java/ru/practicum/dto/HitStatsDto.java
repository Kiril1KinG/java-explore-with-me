package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HitStatsDto {

    private String app;
    private String uri;
    private long hits;
}
