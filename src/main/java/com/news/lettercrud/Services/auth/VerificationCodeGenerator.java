package com.news.lettercrud.Services.auth;

import com.news.lettercrud.Data.DTOs.VerificationCode;

public interface VerificationCodeGenerator {
    VerificationCode generate();
}
