package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.EventState;

@Getter
@Setter
public class EventFullDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private Integer participantLimit = 0;
    private boolean requestModeration = true;
    private EventState state;
    private String title;
    private Long views;
}
