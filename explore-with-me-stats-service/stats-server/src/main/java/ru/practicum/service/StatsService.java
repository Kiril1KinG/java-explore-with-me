package ru.practicum.service;

import ru.practicum.entity.HitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void addHit(HitEntity entity);

    List<HitEntity> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
