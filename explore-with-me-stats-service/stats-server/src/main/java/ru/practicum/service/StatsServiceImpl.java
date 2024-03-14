package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.entity.HitEntity;
import ru.practicum.repository.StatsRepository;
import ru.practicum.repository.StatsSpecs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService{

    private final StatsRepository repository;

    @Override
    public void addHit(HitEntity entity) {
        repository.save(entity);
    }

    @Override
    public List<HitEntity> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Specification<HitEntity> StatsSpec = Specification.where(StatsSpecs.urisIn(uris))
                .and(StatsSpecs.isStartGreaterThanOrEqualTo(start))
                .and(StatsSpecs.isEndLessThanOrEqualTo(end));
        List<HitEntity> entities = repository.findAll(StatsSpec, Sort.by("app", "uri", "ip"));

        List<HitEntity> result = new ArrayList<>();
        int idx = 0;
        for (int i = 0; i < entities.size(); i++) {
            String curUri = entities.get(i).getUri();
            String curIp = entities.get(i).getIp();
            if (i != 0) {
                if (entities.get(i - 1).getUri().equals(curUri)) {
                    if (unique && entities.get(i - 1).getIp().equals(curIp)) {
                        continue;
                    }
                    result.get(idx).setHits(result.get(idx).getHits() + 1);
                    continue;
                } else {
                    idx++;
                }
            }
            HitEntity endpoint = new HitEntity();
            endpoint.setApp(entities.get(i).getApp());
            endpoint.setUri(entities.get(i).getUri());
            endpoint.setHits(1);
            result.add(endpoint);
        }
        return result.stream()
                .sorted(Comparator.comparing(HitEntity::getHits).reversed())
                .collect(Collectors.toList());
    }
}