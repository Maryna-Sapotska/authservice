package com.innowise.authservice.model.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
}
