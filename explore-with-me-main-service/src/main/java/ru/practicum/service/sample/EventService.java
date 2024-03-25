package ru.practicum.service.sample;

import org.springframework.data.util.Pair;
import ru.practicum.model.entity.Comment;
import ru.practicum.model.entity.Event;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.model.enumiration.ParticipationStatus;
import ru.practicum.model.filter.AdminEventFilter;
import ru.practicum.model.filter.PublicEventFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

public interface EventService {

    Event addEvent(Event event);

    Collection<Event> getEventsByCurrentUser(Long userId, Integer from, Integer size);

    Event getEventByCurrentUser(Long userId, Long eventId);

    Event updateEventByCurrentUser(Event event);

    List<Event> getEventsByFiltersForAdmin(AdminEventFilter filter);

    Event updateEventByAdmin(Event event);

    Event getEvent(Long eventId, HttpServletRequest httpServletRequest);

    List<Event> getEventsByFilters(PublicEventFilter filter, HttpServletRequest httpServletRequest);

    Collection<ParticipationRequest> getRequestsForCurrentUser(Long userId, Long eventId);

    Pair<List<ParticipationRequest>, List<ParticipationRequest>> updateRequestsStatusForCurrentUser(Long userId, Long eventId, List<Long> requestsIds, ParticipationStatus status);

    Comment addComment(Comment comment);

    void removeComment(Long userId, Long eventId, Long commentId);

}
