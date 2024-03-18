package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    @NotNull
    @NotEmpty
    private List<Integer> events;

    private Boolean pinned = false;

    @Length(min = 1, max = 50)
    private String title;
}
