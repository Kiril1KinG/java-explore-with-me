package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NewCategoryDto {

    @Length(min = 1, max = 50)
    @NotEmpty
    @NotBlank
    private String name;
}
