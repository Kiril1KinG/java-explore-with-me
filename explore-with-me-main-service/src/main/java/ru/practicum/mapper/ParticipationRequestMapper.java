package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.entity.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    LocalDateTime ldt = LocalDateTime.now();
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "requester", source = "participationRequest.requester.id")
    @Mapping(target = "event", source = "participationRequest.event.id")
    @Mapping(target = "created", expression = "java(participationRequest.getCreated().format(FORMATTER))")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);
}
