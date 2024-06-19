package ru.practicum.controller.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventFullDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.filter.PublicEventFilter;
import ru.practicum.service.sample.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@Slf4j
@AllArgsConstructor
@Validated
public class PublicEventController {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public Collection<EventFullDto> getEventsByFilters(@RequestParam(required = false) String text,
                                                       @RequestParam(required = false) List<Long> categories,
                                                       @RequestParam(required = false) Boolean paid,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                                       @RequestParam(required = false) Boolean onlyAvailable,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
                                                       HttpServletRequest httpServletRequest) {
        log.info("GET /events");
        return eventService.getEventsByFilters(
                        new PublicEventFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size), httpServletRequest).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable @Min(1) Long eventId,
                                 HttpServletRequest httpServletRequest) {
        log.info("GET /events/{}", eventId);
        return eventMapper.toEventFullDto(eventService.getEvent(eventId, httpServletRequest));

    }
}
