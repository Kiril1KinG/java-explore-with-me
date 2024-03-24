package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.enumiration.EventState;
import ru.practicum.model.filter.AdminEventFilter;
import ru.practicum.service.sample.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EventMapper eventMapper;
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByFilters(@RequestParam(required = false) List<Long> users,
                                                 @RequestParam(required = false) List<EventState> states,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                                 @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("GET admin/events");
        return eventService.getEventsByFiltersForAdmin(
                        new AdminEventFilter(users, states, categories, rangeStart, rangeEnd, from, size)).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH admin/events/{}", eventId);
        return eventMapper.toEventFullDto(eventService.updateEventByAdmin(eventMapper.toEvent(eventId, updateEventAdminRequest)));
    }
}
