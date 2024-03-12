package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.entity.HitEntity;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    HitEntity toEntity(HitCreateDto dto);

    HitStatsDto toHitStats(HitEntity entity);
}
