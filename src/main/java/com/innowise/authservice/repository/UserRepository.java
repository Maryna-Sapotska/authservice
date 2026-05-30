package com.innowise.authservice.repository;

import com.innowise.authservice.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
