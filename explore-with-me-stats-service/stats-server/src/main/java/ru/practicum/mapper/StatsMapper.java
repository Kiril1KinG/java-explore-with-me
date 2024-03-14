package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.entity.HitEntity;
import ru.practicum.validation.DateTimeValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DateTimeValidator.DATE_TIME_PATTERN);
    LocalDateTime ldt = LocalDateTime.now();

    @Mapping(target = "timestamp", expression = "java(ldt.parse(dto.getTimestamp(), FORMATTER))")
    HitEntity toEntity(HitCreateDto dto);

    HitStatsDto toHitStats(HitEntity entity);
}
