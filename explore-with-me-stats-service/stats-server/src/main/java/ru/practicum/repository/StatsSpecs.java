package ru.practicum.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.entity.HitEntity;

import java.time.Instant;
import java.util.List;

public abstract class StatsSpecs{

    public static Specification<HitEntity> isStartGreaterThanOrEqualTo(Instant start) {
        if (start == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), start));
    }

    public static Specification<HitEntity> isEndLessThanOrEqualTo(Instant end) {
        if (end == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), end));
    }

    public static Specification<HitEntity> in(List<String> uri) {
        if (uri == null || uri.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("uri").in(uri)));
    }

    public static Specification<HitEntity> unique(String ip) {
        if (ip == null) {
            return null;
        }
        //TODO
        return null;
    }
}
