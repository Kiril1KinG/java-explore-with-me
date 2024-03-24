package ru.practicum.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ParticipationLimitException;
import ru.practicum.exception.ParticipationRequestConflictException;
import ru.practicum.exception.StateException;
import ru.practicum.model.entity.Event;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.model.entity.User;
import ru.practicum.model.enumiration.EventState;
import ru.practicum.model.enumiration.ParticipationStatus;
import ru.practicum.repository.jpaRepository.EventRepository;
import ru.practicum.repository.jpaRepository.ParticipationRequestRepository;
import ru.practicum.repository.jpaRepository.UserRepository;
import ru.practicum.repository.specificaton.ParticipationRequestSpec;
import ru.practicum.service.sample.ParticipationRequestService;
import ru.practicum.service.util.EventEnricher;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final EventEnricher enricher;

    @Override
    public Collection<ParticipationRequest> getParticipationRequestsForCurrentUser(Long userId) {
        findUserById(userId);
        Specification<ParticipationRequest> spec = Specification.where(ParticipationRequestSpec.requesterIdEquals(userId));
        return requestRepository.findAll(spec, Sort.by("id"));
    }

    @Override
    public ParticipationRequest addParticipationRequest(Long userId, Long eventId) {
        User requester = findUserById(userId);
        Event event = findEventById(eventId);
        event.setConfirmedRequests(enricher.getConfirmedParticipationRequests(List.of(eventId)).get(eventId));
        if (event.getState() != EventState.PUBLISHED) {
            throw new StateException("Сannot participate in an unpublished event.");
        }
        if (event.getInitiator().getId().equals(requester.getId())) {
            throw new ParticipationRequestConflictException("User cannot participate in his own event.");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ParticipationLimitException("The limit of participation requests has been reached.");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            participationRequest.setStatus(ParticipationStatus.PENDING);
        } else {
            participationRequest.setStatus(ParticipationStatus.CONFIRMED);
        }

        return requestRepository.save(participationRequest);
    }

    @Override
    public ParticipationRequest cancelParticipationRequest(Long userId, Long requestId) {
        User requester = findUserById(userId);
        ParticipationRequest request = findRequestById(requestId);
        if (!request.getRequester().getId().equals(requester.getId())) {
            throw new ParticipationRequestConflictException(
                    String.format("The user with id %d did not create an request for participation in the event with id %d", userId, requestId));
        }
        request.setStatus(ParticipationStatus.CANCELED);
        return requestRepository.save(request);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("User with id %d not exists.", userId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Event with id %d not exists.", eventId)));
    }

    private ParticipationRequest findRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Participation request with id %d not exists.", requestId)));
    }
}
