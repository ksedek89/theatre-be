package pl.aswit.theatre.rest.dto.register.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterUserRequestDto{
    @NotBlank(message = "may not be null")
    @Email
    private String email;
    private String name;
    private String surname;
}
