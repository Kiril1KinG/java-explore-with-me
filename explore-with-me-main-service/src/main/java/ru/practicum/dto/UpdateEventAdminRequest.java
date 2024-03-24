package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enumiration.AdminEventUpdateStateAction;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 3, max = 120)
    private String title;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
    // Valid value example: 2023-12-34 12:34:57
    // Format yyyy-MM-dd HH:mm:ss
    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private AdminEventUpdateStateAction stateAction;
}
