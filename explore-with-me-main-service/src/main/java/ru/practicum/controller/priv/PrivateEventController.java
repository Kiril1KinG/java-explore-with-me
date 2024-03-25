package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UpdateEventUserRequest;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.service.sample.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService service;
    private final EventMapper mapper;
    private final ParticipationRequestMapper requestMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public Collection<EventShortDto> getEventsByCurrentUser(@PathVariable @Min(1) Long userId,
                                                            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("GET /users/{}/events", userId);
        return service.getEventsByCurrentUser(userId, from, size).stream()
                .map(mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @Min(1) Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST /users/{}/events", userId);
        return mapper.toEventFullDto(service.addEvent(mapper.toEvent(userId, newEventDto)));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByCurrentUser(@PathVariable @Min(1) Long userId,
                                              @PathVariable @Min(1) Long eventId) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        return mapper.toEventFullDto(service.getEventByCurrentUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByCurrentUser(@PathVariable @Min(1) Long userId,
                                                 @PathVariable @Min(1) Long eventId,
                                                 @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH /users/{}/events/{}", userId, eventId);
        return mapper.toEventFullDto(service.updateEventByCurrentUser(mapper.toEvent(userId, eventId, updateEventUserRequest)));
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getParticipationRequestsForCurrentUser(@PathVariable @Min(1) Long userId,
                                                                                      @PathVariable @Min(1) Long eventId) {
        log.info("GET /users/{}/events/{}/requests", userId, eventId);
        return service.getRequestsForCurrentUser(userId, eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsStatusesForCurrentUser(@PathVariable @Min(1) Long userId,
                                                                                    @PathVariable @Min(1) Long eventId,
                                                                                    @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH /users/{}/events/{}/requests", userId, eventId);
        Pair<List<ParticipationRequest>, List<ParticipationRequest>> requests = service.updateRequestsStatusForCurrentUser(userId, eventId,
                eventRequestStatusUpdateRequest.getRequestIds(), eventRequestStatusUpdateRequest.getStatus());

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(requests.getFirst().stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));
        result.setRejectedRequests(requests.getSecond().stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));
        return result;
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable @Min(1) Long userId,
                                 @PathVariable @Min(1) Long eventId,
                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST /users/{}/events/{}/comments", userId, eventId);
        return commentMapper.toCommentDto(service.addComment(commentMapper.toComment(userId, eventId, newCommentDto)));
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable @Min(1) Long userId,
                              @PathVariable @Min(1) Long eventId,
                              @PathVariable @Min(1) Long commentId) {
        log.info("POST /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        service.removeComment(userId, eventId, commentId);
    }
}
