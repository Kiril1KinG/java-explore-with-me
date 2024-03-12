package ru.practicum.service;

import ru.practicum.entity.HitEntity;

import java.time.Instant;
import java.util.List;

public interface StatsService {

    HitEntity addHit(HitEntity entity);

    List<HitEntity> getStats(Instant start, Instant end, List<String> uris, Boolean unique);
}
