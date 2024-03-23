package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.enumiration.UserUpdateEventStateAction;

import javax.validation.constraints.Min;

@Getter
@Setter
public class UpdateEventUserRequest {

    @Length(min = 20, max = 2000)
    private String annotation;

    @Length(min = 20, max = 7000)
    private String description;

    @Length(min = 3, max = 120)
    private String title;

    @Min(1)
    private Long category;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    @Min(0)
    private Integer participantLimit;

    private Boolean requestModeration;

    private UserUpdateEventStateAction stateAction;
}
