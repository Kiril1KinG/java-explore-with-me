package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateCompilationRequest {

    private List<Long> events;
    private boolean pinned;
    private String title;
}
