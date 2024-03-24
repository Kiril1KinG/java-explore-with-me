package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
