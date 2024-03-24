package ru.practicum.repository.specificaton;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.model.enumiration.ParticipationStatus;

import java.util.List;

public class ParticipationRequestSpec {

    public static Specification<ParticipationRequest> requesterIdEquals(Long userId) {
        if (userId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requester").get("id"), userId);
    }

    public static Specification<ParticipationRequest> eventIdEquals(Long eventId) {
        if (eventId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("event").get("id"), eventId);
    }

    public static Specification<ParticipationRequest> eventIdsIn(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("event").get("id")).value(eventIds);
    }

    public static Specification<ParticipationRequest> idsIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(ids);
    }

    public static Specification<ParticipationRequest> statusIs(ParticipationStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
}
