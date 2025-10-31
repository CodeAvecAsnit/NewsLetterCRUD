package com.news.lettercrud.repository;

import com.news.lettercrud.data.DTOs.LoginAttempt;

import java.util.Optional;

public interface VerificationCodeRepository {
    void store(String email, LoginAttempt attempt);
    Optional<LoginAttempt> find(String email);
    void remove(String email);
    boolean exists(String email);
}