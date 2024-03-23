package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
public class UpdateCompilationRequest {

    private Set<Long> events;
    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;
}
