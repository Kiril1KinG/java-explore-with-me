package ru.practicum.model.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enumiration.EventState;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AdminEventFilter {
    private final List<Long> users;
    private final List<EventState> states;
    private final List<Long> categories;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Integer from;
    private final Integer size;
}
