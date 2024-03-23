package ru.practicum.service.sample;

import ru.practicum.model.entity.ParticipationRequest;

import java.util.Collection;

public interface ParticipationRequestService {

    Collection<ParticipationRequest> getParticipationRequestsForCurrentUser(Long userId);

    ParticipationRequest addParticipationRequest(Long userId, Long eventId);

    ParticipationRequest cancelParticipationRequest(Long userId, Long requestId);

}
