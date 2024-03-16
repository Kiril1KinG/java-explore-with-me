package ru.practicum.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.entity.HitEntity;

import java.time.LocalDateTime;
import java.util.List;

public abstract class StatsSpecs {

    public static Specification<HitEntity> isStartGreaterThanOrEqualTo(LocalDateTime start) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), start));
    }

    public static Specification<HitEntity> isEndLessThanOrEqualTo(LocalDateTime end) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), end));
    }

    public static Specification<HitEntity> urisIn(List<String> uris) {
        return ((root, query, criteriaBuilder) -> {
            if (uris == null || uris.isEmpty()) {
                return null;
            } else {
                return criteriaBuilder.in(root.get("uri")).value(uris);
            }
        });
    }
}
