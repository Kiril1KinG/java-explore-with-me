package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class NewEventDto {

    @NotEmpty
    @NotBlank
    private String annotation;

    @NotNull
    @Min(1)
    private Long category;

    @NotBlank
    @NotEmpty
    private String title;

    @NotBlank
    @NotEmpty
    private String description;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
    private String eventDate;

    @NotNull
    private Location location;

    private boolean paid = false;

    private Integer participantLimit = 0;

    private boolean requestModeration = true;
}
