package ru.practicum.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.entity.Comment;
import ru.practicum.model.entity.Compilation;
import ru.practicum.model.entity.Event;
import ru.practicum.repository.jpaRepository.CompilationRepository;
import ru.practicum.repository.jpaRepository.EventRepository;
import ru.practicum.repository.specificaton.CompilationSpec;
import ru.practicum.service.sample.CompilationService;
import ru.practicum.service.util.EventEnricher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventEnricher enricher;

    @Override
    public Collection<Compilation> getCompilationsByFilters(Boolean pinned, Integer from, Integer size) {
        Specification<Compilation> spec = Specification.where(CompilationSpec.pinned(pinned));
        List<Compilation> compilations = compilationRepository.findAll(spec, PageRequest.of(from / size, size)).getContent();
        enrichEventsInCompilations(compilations, size);
        return compilations;
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        Compilation compilation = findCompilationById(compId);
        enrichEventsInCompilations(List.of(compilation), compilation.getEvents().size());
        return compilation;
    }

    @Override
    public Compilation addCompilation(Compilation compilation) {
        if (compilation.getEvents() != null) {
            List<Event> events = compilation.getEvents().stream()
                    .map(e -> findEventById(e.getId()))
                    .collect(Collectors.toList());
            compilation.setEvents(new HashSet<>(events));
            enrichEventsInCompilations(List.of(compilation), compilation.getEvents().size());
        }
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation updateCompilation(Compilation compilation) {
        Compilation entity = findCompilationById(compilation.getId());

        if (compilation.getEvents() != null) {
            List<Event> events = compilation.getEvents().stream()
                    .map(e -> findEventById(e.getId()))
                    .collect(Collectors.toList());
            enrichEventsInCompilations(List.of(compilation), compilation.getEvents().size());
            entity.setEvents(new HashSet<>(events));
        }
        if (compilation.getPinned() != null) {
            entity.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            entity.setTitle(compilation.getTitle());
        }
        return compilationRepository.save(entity);
    }

    @Override
    public void removeCompilationById(Long compId) {
        findCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    private Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Compilation with id %d not found", compId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }

    private void enrichEventsInCompilations(List<Compilation> compilations, Integer size) {
        List<Event> events = new ArrayList<>();
        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        compilations.forEach(comp -> events.addAll(new ArrayList<>(comp.getEvents())));

        Map<Long, Long> views = enricher.getEventsViews(events);
        Map<Long, Integer> confirmedRequests = enricher.getConfirmedParticipationRequests(eventsIds);
        Map<Long, List<Comment>> comments = enricher.getEventsComments(eventsIds);
        events.forEach(event -> {
            event.setViews(views.getOrDefault(event.getId(), 0L));
            event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0));
            event.setComments(comments.getOrDefault(comments.get(event.getId()), new ArrayList<>()));
        });
    }
}
