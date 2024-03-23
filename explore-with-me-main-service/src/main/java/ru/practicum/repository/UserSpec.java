package ru.practicum.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.User;

import java.util.Collection;

public class UserSpec {

    public static Specification<User> idIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(ids));
    }
}
