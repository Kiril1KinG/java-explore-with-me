package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompilationDto {

    private Long id;
    private String title;
    private List<EventShortDto> events;
    private Boolean pinned;
}
