package ru.practicum.repository.specificaton;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.entity.Comment;

import java.util.List;

public class CommentSpec {

    public static Specification<Comment> eventIdsIn(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("event").get("id")).value(eventIds);
    }
}
