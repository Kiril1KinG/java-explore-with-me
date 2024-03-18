package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class NewCategoryDto {

    @Length(min = 1, max = 50)
    private String name;
}
