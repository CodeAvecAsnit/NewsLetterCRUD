package com.news.lettercrud.service.components;

import com.news.lettercrud.data.dto.LoginAttempt;

import java.util.Optional;

public interface VerificationCodeRepository {
    void store(String email, LoginAttempt attempt);
    Optional<LoginAttempt> find(String email);
    void remove(String email);
    boolean exists(String email);
}