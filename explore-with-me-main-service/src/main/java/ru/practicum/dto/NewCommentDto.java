package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewCommentDto {

    @NotEmpty
    @NotBlank
    @NotNull
    @Size(min = 1, max = 1000)
    private String text;
}
