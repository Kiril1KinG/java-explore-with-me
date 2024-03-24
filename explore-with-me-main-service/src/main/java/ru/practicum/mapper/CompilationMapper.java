package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.model.entity.Compilation;
import ru.practicum.model.entity.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "events", qualifiedByName = "mapEvents")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(target = "events", qualifiedByName = "mapEvents")
    @Mapping(target = "id", source = "compId")
    Compilation toCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    @Named("mapEvents")
    default Set<Event> mapIdsToEvents(Set<Long> events) {
        if (events != null) {
            return events.stream()
                    .map(e -> {
                        Event event = new Event();
                        event.setId(e);
                        return event;
                    })
                    .collect(Collectors.toSet());
        }
        return null;
    }
}
