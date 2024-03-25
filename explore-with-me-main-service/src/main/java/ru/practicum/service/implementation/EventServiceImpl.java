package ru.practicum.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.exception.BadQueryParamsException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.ParticipationLimitException;
import ru.practicum.exception.ParticipationRequestConflictException;
import ru.practicum.exception.StateException;
import ru.practicum.exception.UnknownParamException;
import ru.practicum.model.entity.Category;
import ru.practicum.model.entity.Comment;
import ru.practicum.model.entity.Event;
import ru.practicum.model.entity.ParticipationRequest;
import ru.practicum.model.entity.User;
import ru.practicum.model.enumiration.AdminEventUpdateStateAction;
import ru.practicum.model.enumiration.EventState;
import ru.practicum.model.enumiration.ParticipationStatus;
import ru.practicum.model.enumiration.UserUpdateEventStateAction;
import ru.practicum.model.filter.AdminEventFilter;
import ru.practicum.model.filter.PublicEventFilter;
import ru.practicum.repository.jpaRepository.CategoryRepository;
import ru.practicum.repository.jpaRepository.CommentRepository;
import ru.practicum.repository.jpaRepository.EventRepository;
import ru.practicum.repository.jpaRepository.ParticipationRequestRepository;
import ru.practicum.repository.jpaRepository.UserRepository;
import ru.practicum.repository.specificaton.EventSpec;
import ru.practicum.repository.specificaton.ParticipationRequestSpec;
import ru.practicum.service.sample.EventService;
import ru.practicum.service.util.EventEnricher;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String APP = "ewm-main-service";

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatsClient statsClient;
    private final EventEnricher enricher;

    @Override
    public Event addEvent(Event event) {
        checkEventDate(event.getEventDate());
        event.setCategory(findCategoryById(event.getCategory().getId()));
        event.setInitiator(findUserById(event.getInitiator().getId()));
        return eventRepository.save(event);
    }

    @Override
    public Collection<Event> getEventsByCurrentUser(Long userId, Integer from, Integer size) {
        Specification<Event> spec = Specification.where(EventSpec.initiatorIdEquals(userId));
        List<Event> events = eventRepository.findAll(spec, PageRequest.of(from / size, size, Sort.by("id"))).getContent();
        for (Event event : events) {
            event.setViews(enricher.getEventViews(event));
        }
        return events;
    }

    @Override
    public Event getEventByCurrentUser(Long userId, Long eventId) {
        User initiator = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new DataNotFoundException(
                    String.format("Event with id %d not found for user with id %d.", eventId, userId));
        }
        event.setViews(enricher.getEventViews(event));
        return event;
    }

    @Override
    public Event updateEventByCurrentUser(Event event) {
        User initiator = findUserById(event.getInitiator().getId());
        Event eventEntity = findEventById(event.getId());
        if (!eventEntity.getInitiator().getId().equals(initiator.getId())) {
            throw new DataNotFoundException(
                    String.format("Event with id %d not found for user with id %d.", event.getId(), event.getInitiator().getId()));
        }
        if (eventEntity.getState().equals(EventState.PUBLISHED)) {
            throw new StateException("Can't update a published event.");
        }

        if (event.getAnnotation() != null) {
            eventEntity.setAnnotation(event.getAnnotation());
        }
        if (event.getDescription() != null) {
            eventEntity.setDescription(event.getDescription());
        }
        if (event.getTitle() != null) {
            eventEntity.setTitle(event.getTitle());
        }
        if (event.getCategory().getId() != null) {
            eventEntity.setCategory(findCategoryById(event.getCategory().getId()));
        }
        if (event.getEventDate() != null) {
            checkEventDate(event.getEventDate());
            eventEntity.setEventDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            eventEntity.setLocation(event.getLocation());
        }
        if (event.getPaid() != null) {
            eventEntity.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            eventEntity.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            eventEntity.setRequestModeration(event.getRequestModeration());
        }
        if (event.getStateAction() != null) {
            if (event.getStateAction() == UserUpdateEventStateAction.SEND_TO_REVIEW) {
                eventEntity.setState(EventState.PENDING);
            }
            if (event.getStateAction() == UserUpdateEventStateAction.CANCEL_REVIEW) {
                eventEntity.setState(EventState.CANCELED);
            }
        }
        return eventRepository.save(eventEntity);
    }

    @Override
    public Collection<ParticipationRequest> getRequestsForCurrentUser(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        findUserById(userId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ParticipationRequestConflictException(
                    String.format("User with id %d not initiator of event with id %d", userId, eventId));
        }
        Specification<ParticipationRequest> spec = Specification.where(ParticipationRequestSpec.eventIdEquals(eventId));
        return requestRepository.findAll(spec);
    }

    @Override
    public Pair<List<ParticipationRequest>, List<ParticipationRequest>> updateRequestsStatusForCurrentUser(Long userId, Long eventId, List<Long> requestsIds, ParticipationStatus status) {
        findUserById(userId);
        Event event = findEventById(eventId);
        event.setConfirmedRequests(enricher.getConfirmedParticipationRequests(List.of(eventId)).getOrDefault(eventId, 0));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ParticipationRequestConflictException(
                    String.format("User with id %d not initiator of event with id %d", userId, eventId));
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ParticipationRequestConflictException(
                    String.format("For event with id %d moderation is not required.", eventId));
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ParticipationLimitException(
                    String.format("Participation requests limit for event with id %d has been reached.", eventId));
        }

        Specification<ParticipationRequest> spec = Specification.where(ParticipationRequestSpec.idsIn(requestsIds));
        List<ParticipationRequest> requests = requestRepository.findAll(spec);

        List<ParticipationRequest> confirmed = new ArrayList<>();
        List<ParticipationRequest> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != ParticipationStatus.PENDING) {
                throw new ParticipationRequestConflictException("You can only change the status of pending requests.");
            }
            if (event.getParticipantLimit() >= event.getConfirmedRequests() && status == ParticipationStatus.CONFIRMED) {
                request.setStatus(ParticipationStatus.CONFIRMED);
                confirmed.add(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                request.setStatus(ParticipationStatus.REJECTED);
                rejected.add(request);
            }
        }
        requestRepository.saveAll(Stream.concat(confirmed.stream(), rejected.stream())
                .collect(Collectors.toList()));

        return Pair.of(confirmed, rejected);
    }

    @Override
    public List<Event> getEventsByFiltersForAdmin(AdminEventFilter filter) {
        Specification<Event> spec = Specification.where(EventSpec.initiatorsIdsIn(filter.getUsers()))
                .and(EventSpec.statesIn(filter.getStates()))
                .and(EventSpec.categoriesIdsIn(filter.getCategories()))
                .and(EventSpec.eventDateAfterOrEqual(filter.getRangeStart()))
                .and(EventSpec.eventDateBeforeOrEqual(filter.getRangeEnd()));
        List<Event> events = eventRepository.findAll(spec, PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize())).getContent();

        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> confirmedRequests = enricher.getConfirmedParticipationRequests(eventsIds);
        events.forEach(event -> event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0)));

        Map<Long, Long> views = enricher.getEventsViews(events);
        events.forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));

        Map<Long, List<Comment>> comments = enricher.getEventsComments(eventsIds);
        events.forEach(event -> event.setComments(comments.getOrDefault(event.getId(), new ArrayList<>())));
        return events;
    }

    @Override
    public Event updateEventByAdmin(Event event) {
        Event eventEntity = findEventById(event.getId());
        if (eventEntity.getState() != EventState.PENDING) {
            throw new StateException("An event can be updated only in pending state.");
        }
        if (eventEntity.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new DateTimeException("The start date of the event to be modified must be no earlier than one hour from the date of publication.");
        }

        if (event.getAnnotation() != null) {
            eventEntity.setAnnotation(event.getAnnotation());
        }
        if (event.getTitle() != null) {
            eventEntity.setTitle(event.getTitle());
        }
        if (event.getCategory().getId() != null) {
            eventEntity.setCategory(findCategoryById(event.getCategory().getId()));
        }
        if (event.getDescription() != null) {
            eventEntity.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            checkEventDate(event.getEventDate());
            eventEntity.setEventDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            eventEntity.setLocation(event.getLocation());
        }
        if (event.getPaid() != null) {
            eventEntity.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            eventEntity.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            eventEntity.setRequestModeration(event.getRequestModeration());
        }
        if (event.getAdminStateAction() != null) {
            if (event.getAdminStateAction() == AdminEventUpdateStateAction.PUBLISH_EVENT) {
                eventEntity.setState(EventState.PUBLISHED);
            }
            if (event.getAdminStateAction() == AdminEventUpdateStateAction.REJECT_EVENT) {
                eventEntity.setState(EventState.CANCELED);
            }
        }
        eventEntity.setPublishedOn(LocalDateTime.now());
        return eventRepository.save(eventEntity);
    }

    @Override
    public List<Event> getEventsByFilters(PublicEventFilter filter, HttpServletRequest httpRequest) {
        checkStartAndEndDate(filter.getRangeStart(), filter.getRangeEnd());
        Specification<Event> spec = Specification.where(EventSpec.stateEqual(EventState.PUBLISHED))
                .and(EventSpec.categoriesIdsIn(filter.getCategories()))
                .and(EventSpec.paidEqual(filter.getPaid()))
                .and(EventSpec.eventDateAfterOrEqual(filter.getRangeStart()))
                .and(EventSpec.eventDateBeforeOrEqual(filter.getRangeEnd()))
                .and(EventSpec.annotationOrDescriptionContains(filter.getText()));
        if (filter.getRangeStart() == null && filter.getRangeEnd() == null) {
            spec.and(EventSpec.eventDateAfterOrEqual(LocalDateTime.now()));
        }

        List<Event> events = new ArrayList<>();
        Map<Long, Long> idsAndViews = new HashMap<>();
        Map<Long, Integer> idsAndConfirmedRequests;

        if (filter.getSort() != null) {
            if ((!filter.getSort().equalsIgnoreCase("EVENT_DATE")) && (!filter.getSort().equalsIgnoreCase("VIEWS"))) {
                throw new UnknownParamException(
                        String.format("Unsupported sort param %s.", filter.getSort()));
            }
            if (filter.getSort().equals("EVENT_DATE")) {
                Pageable pageable = PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize(), Sort.by("eventDate").descending());
                events = eventRepository.findAll(spec, pageable).getContent();
                idsAndViews = enricher.getEventsViews(events);
            }
            if (filter.getSort().equals("VIEWS")) {
                idsAndViews = enricher.getEventsViews(null);
                events = eventRepository.findAll(spec.and(EventSpec.idsIn(new ArrayList<>(idsAndViews.keySet()))),
                        PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize())).getContent();
            }
        } else {
            events = eventRepository.findAll(PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize())).getContent();
        }
        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        idsAndConfirmedRequests = enricher.getConfirmedParticipationRequests(eventsIds);
        Map<Long, List<Comment>> comments = enricher.getEventsComments(eventsIds);

        for (Event event : events) {
            event.setViews(idsAndViews.getOrDefault(event.getId(), 0L));
            event.setConfirmedRequests(idsAndConfirmedRequests.getOrDefault(event.getId(), 0));
            event.setComments(comments.getOrDefault(event.getId(), new ArrayList<>()));
            statsClient.addHit(APP, "/events/" + event.getId(), httpRequest.getRemoteAddr());
        }
        statsClient.addHit(APP, "/events", httpRequest.getRemoteAddr());
        return events;
    }

    @Override
    public Event getEvent(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = findEventById(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new DataNotFoundException("Event not published.");
        }
        event.setViews(enricher.getEventViews(event));
        event.setConfirmedRequests(enricher.getConfirmedParticipationRequests(List.of(eventId)).getOrDefault(eventId, 0));
        event.setComments(enricher.getEventsComments(List.of(eventId)).getOrDefault(eventId, new ArrayList<>()));
        statsClient.addHit(APP, "/events/" + eventId, httpServletRequest.getRemoteAddr());
        return event;
    }

    @Override
    public Comment addComment(Comment comment) {
        Event event = findEventById(comment.getEvent().getId());
        comment.setUser(findUserById(comment.getUser().getId()));
        if (event.getState() != EventState.PUBLISHED) {
            throw new DataNotFoundException(
                    String.format("Event with id %d not published", event.getId()));
        }
        return commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long userId, Long eventId, Long commentId) {
        findEventById(eventId);
        findUserById(userId);
        Comment comment = findCommentById(commentId);
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new DataNotFoundException(
                    String.format("Comment with id %d not found for event with id %d", commentId, eventId));
        }
        if (!comment.getUser().getId().equals(userId)) {
            throw new DataNotFoundException(
                    String.format("Comment with id %d not found for user with id %d", commentId, userId));
        }
        commentRepository.deleteById(commentId);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadQueryParamsException("Event date can't be earlier than two hours.");
        }
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new BadQueryParamsException("Event date can't be earlier than now.");
        }
    }

    private void checkStartAndEndDate(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new BadQueryParamsException("Start date cannot be after end date");
            }
        }
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Category with id %d not exists.", categoryId)));
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

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Comment with id %d not exists.", commentId)));
    }
}
