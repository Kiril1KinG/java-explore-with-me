package ru.practicum.repository.specificaton;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.entity.Compilation;

public class CompilationSpec {

    public static Specification<Compilation> pinned(Boolean pinned) {
        if (pinned == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned);
    }
}
