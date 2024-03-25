package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enumiration.EventState;

import java.util.List;

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
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
    private List<CommentDto> comments;
}
