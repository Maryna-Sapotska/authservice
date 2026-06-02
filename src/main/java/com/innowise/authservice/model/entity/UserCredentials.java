package com.innowise.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users_credentials")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean active = true;
}
