package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.StateAction;

@Getter
@Setter
public class UpdateEventAdminRequest {

    @Length(min = 20, max = 2000) private String annotation;
    @Length(min = 3, max = 120) private String title;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
}
