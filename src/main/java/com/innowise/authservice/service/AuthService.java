package com.innowise.authservice.service;

import com.innowise.authservice.exception.*;
import com.innowise.authservice.model.dto.request.LoginRequest;
import com.innowise.authservice.model.dto.request.RegisterRequest;
import com.innowise.authservice.model.dto.response.TokenResponse;
import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.UserCredentials;
import com.innowise.authservice.repository.UserRepository;
import com.innowise.authservice.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public TokenResponse register(RegisterRequest request) {

        if (repository.findByLogin(request.getLogin()).isPresent()) {
            throw new LoginAlreadyExistsException("Login already exists");
        }

        String hashedPassword = encoder.encode(request.getPassword());

        UserCredentials user = UserCredentials.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(Role.ROLE_USER)
                .active(true)
                .build();

        repository.save(user);

        return new TokenResponse(
                tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user)
        );
    }

    public TokenResponse login(LoginRequest request) {

        UserCredentials user = repository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UserCredentialsException("User is not found!"));

        if (!user.isActive()){
            throw new DisabledAccountException("Account is disabled");
        }
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid login or password");
        }

        return new TokenResponse(
                tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user));
    }

    public TokenResponse refresh(String refreshToken) {

        if (!tokenService.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token") {};
        }

        String login = tokenService.extractLogin(refreshToken);

        UserCredentials user = repository.findByLogin(login)
                .orElseThrow(() -> new UserCredentialsException("User is not found"));

        if (!user.isActive()) {
            throw new DisabledAccountException("Account is disabled");
        }

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
