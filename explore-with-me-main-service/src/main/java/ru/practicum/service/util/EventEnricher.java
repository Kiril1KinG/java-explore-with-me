package ru.practicum.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.model.entity.Comment;
import ru.practicum.model.entity.Event;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.model.enumiration.EventState;
import ru.practicum.model.enumiration.ParticipationStatus;
import ru.practicum.repository.jpaRepository.CommentRepository;
import ru.practicum.repository.jpaRepository.ParticipationRequestRepository;
import ru.practicum.repository.specificaton.CommentSpec;
import ru.practicum.repository.specificaton.ParticipationRequestSpec;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventEnricher {

    private static final LocalDateTime MIN_DATE_TIME_FOR_STATS = LocalDateTime.of(1000, 1, 1, 1, 1);

    private final StatsClient statsClient;
    private final ParticipationRequestRepository requestRepository;
    private final CommentRepository commentRepository;

    public Long getEventViews(Event event) {
        if (event.getState() == EventState.PUBLISHED) {
            List<HitStatsDto> stats = statsClient.getStats(MIN_DATE_TIME_FOR_STATS, LocalDateTime.now(), List.of("/events/" + event.getId()), true);
            return stats.size() == 0 ? 0 : stats.get(0).getHits();
        }
        return 0L;
    }

    public Map<Long, Long> getEventsViews(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            uris.add("/events/" + event.getId());
        }
        List<HitStatsDto> stats = statsClient.getStats(MIN_DATE_TIME_FOR_STATS, LocalDateTime.now(), uris, true);

        Map<Long, Long> eventsIdsAndViews = new HashMap<>();
        for (HitStatsDto stat : stats) {
            int charIndex = stat.getUri().substring(1).indexOf("/");
            String value = stat.getUri().substring(charIndex + 2);
            if (value.equals("events")) {
                continue;
            }
            eventsIdsAndViews.put(Long.parseLong(value), stat.getHits());
        }
        return eventsIdsAndViews;
    }

    public Map<Long, Integer> getConfirmedParticipationRequests(List<Long> eventsIds) {
        Specification<ParticipationRequest> spec = Specification.where(ParticipationRequestSpec.eventIdsIn(eventsIds))
                .and(ParticipationRequestSpec.statusIs(ParticipationStatus.CONFIRMED));

        List<ParticipationRequest> requests = requestRepository.findAll(spec, Sort.by("event_id"));

        Map<Long, Integer> eventsIdsAndConfirmedRequests = new HashMap<>();
        for (int i = 0; i < requests.size(); i++) {
            long curEventId = requests.get(i).getEvent().getId();
            if (i != 0) {
                long prevEventId = requests.get(i - 1).getEvent().getId();
                if (prevEventId == curEventId) {
                    eventsIdsAndConfirmedRequests.put(curEventId, eventsIdsAndConfirmedRequests.get(curEventId) + 1);
                    continue;
                }
            }
            eventsIdsAndConfirmedRequests.put(curEventId, 1);
        }
        return eventsIdsAndConfirmedRequests;
    }

    public Map<Long, List<Comment>> getEventsComments(List<Long> eventsIds) {
        Specification<Comment> spec = Specification.where(CommentSpec.eventIdsIn(eventsIds));
        List<Comment> comments = commentRepository.findAll(spec, Sort.by("event_id", "createdOn"));

        Map<Long, List<Comment>> eventIdsAndComments = new HashMap<>();
        for (int i = 0; i < comments.size(); i++) {
            long curEventId = comments.get(i).getEvent().getId();
            if (i != 0) {
                long prevEventId = comments.get(i - 1).getEvent().getId();
                if (prevEventId == curEventId) {
                    List<Comment> commentsForEvent = eventIdsAndComments.get(curEventId);
                    commentsForEvent.add(comments.get(i));
                    eventIdsAndComments.put(curEventId, commentsForEvent);
                    continue;
                }
            }
            eventIdsAndComments.put(curEventId, new ArrayList<>());
            eventIdsAndComments.get(curEventId).add(comments.get(i));
        }
        return eventIdsAndComments;
    }
}
