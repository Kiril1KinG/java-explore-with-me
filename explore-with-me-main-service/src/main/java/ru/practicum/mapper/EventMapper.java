package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.dto.UpdateEventUserRequest;
import ru.practicum.model.entity.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class, LocationMapper.class, CommentMapper.class})
public interface EventMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime ldt = LocalDateTime.now();

    @Mapping(target = "category.id", source = "newEventDto.category")
    @Mapping(target = "initiator.id", source = "userId")
    @Mapping(target = "eventDate", expression = "java(ldt.parse(newEventDto.getEventDate(), FORMATTER))")
    Event toEvent(Long userId, NewEventDto newEventDto);

    @Mapping(target = "category.id", source = "updateEventUserRequest.category")
    @Mapping(target = "eventDate", qualifiedByName = "parseDateTime")
    @Mapping(target = "initiator.id", source = "userId")
    @Mapping(target = "id", source = "eventId")
    Event toEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    @Mapping(target = "eventDate", qualifiedByName = "parseDateTime")
    @Mapping(target = "id", source = "eventId")
    @Mapping(target = "category.id", source = "updateEventAdminRequest.category")
    @Mapping(target = "adminStateAction", source = "updateEventAdminRequest.stateAction")
    @Mapping(target = "stateAction", ignore = true)
    Event toEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(target = "eventDate", expression = "java(event.getEventDate().format(FORMATTER))")
    @Mapping(target = "createdOn", expression = "java(event.getCreatedOn().format(FORMATTER))")
    @Mapping(target = "comments", source = "event.comments")
    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    @Named("parseDateTime")
    default LocalDateTime parseDateTime(String dt) {
        if (dt != null) {
            return LocalDateTime.parse(dt, FORMATTER);
        }
        return null;
    }

}
