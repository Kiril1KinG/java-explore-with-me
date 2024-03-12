package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.entity.HitEntity;
import ru.practicum.repository.StatsRepository;
import ru.practicum.repository.StatsSpecs;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{

    private final StatsRepository repository;

    @Override
    public HitEntity addHit(HitEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<HitEntity> getStats(Instant start, Instant end, List<String> uris, Boolean unique) {
        Specification<HitEntity> specification = Specification.where(StatsSpecs.isStartGreaterThanOrEqualTo(start))
                .and(StatsSpecs.isEndLessThanOrEqualTo(end))
                .and(StatsSpecs.in(uris));
        return repository.findAll(specification);
    }
}
