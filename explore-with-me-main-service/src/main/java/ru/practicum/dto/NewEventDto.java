package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewEventDto {

    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @Min(1)
    private Long category;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @NotEmpty
    @Size(min = 20, max = 7000)
    private String description;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
    // Valid value example: 2023-12-34 12:34:57
    // Format yyyy-MM-dd HH:mm:ss
    private String eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;
}
