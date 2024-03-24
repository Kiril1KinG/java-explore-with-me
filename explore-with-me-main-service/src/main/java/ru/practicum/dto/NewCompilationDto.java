package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class NewCompilationDto {

    private Set<Long> events;

    private Boolean pinned = false;

    @Length(min = 1, max = 50)
    @NotBlank
    @NotEmpty
    private String title;
}
