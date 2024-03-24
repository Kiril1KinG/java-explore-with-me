package ru.practicum.repository.specificaton;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.entity.Event;
import ru.practicum.model.enumiration.EventState;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpec {

    public static Specification<Event> initiatorIdEquals(Long userId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("initiator").get("id"), userId));
    }

    public static Specification<Event> initiatorsIdsIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(ids);
    }

    public static Specification<Event> statesIn(List<EventState> states) {
        if (states == null || states.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states);
    }

    public static Specification<Event> categoriesIdsIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(ids);
    }

    public static Specification<Event> eventDateAfterOrEqual(LocalDateTime start) {
        if (start == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start);
    }

    public static Specification<Event> eventDateBeforeOrEqual(LocalDateTime end) {
        if (end == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end);
    }

    public static Specification<Event> stateEqual(EventState state) {
        if (state == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), state);
    }

    public static Specification<Event> paidEqual(Boolean paid) {
        if (paid == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> annotationOrDescriptionContains(String text) {
        if (text == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("annotation"), "%" + text + "%"),
                criteriaBuilder.like(root.get("description"), "%" + text + "%")
        );
    }

    public static Specification<Event> idsIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(ids);
    }
}
