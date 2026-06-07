package com.innowise.authservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ValidateToken {

    @NotBlank
    private String token;
}
