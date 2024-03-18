package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CategoryDto {

    private Long id;

    @Length(min = 1, max = 50)
    private String name;
}
