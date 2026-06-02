package com.innowise.authservice.config;

import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.UserCredentials;
import com.innowise.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder encoder;

    @Value("${jwt.admin-password}")
    private String password;

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {

        return args -> {

            if (userRepository.findByLogin("admin").isEmpty()) {
                userRepository.save(
                        UserCredentials.builder()
                                .login("admin")
                                .email("admin@test.com")
                                .password(encoder.encode(password))
                                .role(Role.ROLE_ADMIN)
                                .active(true)
                                .build()
                );
            }
        };
    }
}
