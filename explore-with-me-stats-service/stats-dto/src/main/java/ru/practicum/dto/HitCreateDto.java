package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Getter
@Setter
public class HitCreateDto {

    @NotEmpty
    @NotBlank
    private String app;

    @NotEmpty
    @NotBlank
    private String uri;

    @NotEmpty
    @NotBlank
    private String ip;

    @NotNull
    private Instant timestamp;
}
