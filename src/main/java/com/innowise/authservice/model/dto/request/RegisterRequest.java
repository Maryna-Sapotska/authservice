package com.innowise.authservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class RegisterRequest {

    @Size(max = 100, message = "Login should be up to 100 characters")
    @NotBlank
    private String login;

    @Email(message = "Email should be as user@example.com")
    @NotBlank
    private String email;

    @Size(min = 4, message = "Password should be more than 4 characters")
    @NotBlank
    private String password;
}
