package com.news.lettercrud.Repositories;

import com.news.lettercrud.Data.DTOs.LoginAttempt;

import java.util.Optional;

public interface VerificationCodeRepository {
    void store(String email, LoginAttempt attempt);
    Optional<LoginAttempt> find(String email);
    void remove(String email);
    boolean exists(String email);
}