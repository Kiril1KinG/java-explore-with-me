package ru.practicum.model.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PublicEventFilter {
    private final String text;
    private final List<Long> categories;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Boolean onlyAvailable;
    private final String sort;
    private final Integer from;
    private final Integer size;
}
