package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
    // valid format example: 2023-12-06 12:34:56
    // yyyy-MM-dd HH:mm:ss
    private String timestamp;
}
