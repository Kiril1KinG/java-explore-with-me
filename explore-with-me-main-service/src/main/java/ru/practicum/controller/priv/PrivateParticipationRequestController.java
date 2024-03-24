package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.service.sample.ParticipationRequestService;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateParticipationRequestController {

    private final ParticipationRequestService requestService;
    private final ParticipationRequestMapper requestMapper;


    @GetMapping
    public Collection<ParticipationRequestDto> getParticipationRequestsForCurrentUser(@PathVariable @Min(1) Long userId) {
        log.info("GET /users/{}/requests", userId);
        return requestService.getParticipationRequestsForCurrentUser(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable @Min(1) Long userId, @RequestParam @Min(1) Long eventId) {
        log.info("POST /users/{}/requests?eventId={}", userId, eventId);
        return requestMapper.toParticipationRequestDto(requestService.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Min(1) Long userId,
                                                              @PathVariable @Min(1) Long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return requestMapper.toParticipationRequestDto(requestService.cancelParticipationRequest(userId, requestId));
    }
}
