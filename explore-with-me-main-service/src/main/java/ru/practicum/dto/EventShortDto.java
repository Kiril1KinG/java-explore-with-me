package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import ru.practicum.dto.UserShortDto;

@Getter
@Setter
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
}
