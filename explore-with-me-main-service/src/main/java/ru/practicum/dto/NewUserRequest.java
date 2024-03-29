package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NewUserRequest {

    @Email
    @NotEmpty
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;

    @NotEmpty
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}
