package com.innowise.authservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Size(max = 100, message = "Login should be up to 100 characters")
    @NotBlank
    private String login;

    @Size(min = 4, message = "Password should be more than 4 characters")
    @NotBlank
    private String password;
}
