package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@ToString
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
    @NotEmpty
    @Pattern(regexp = "^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d")
    private String timestamp;
}
