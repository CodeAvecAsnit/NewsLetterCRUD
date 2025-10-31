package com.news.lettercrud.service.auth;

import com.news.lettercrud.data.DTOs.VerificationCode;

public interface VerificationCodeGenerator {
    VerificationCode generate();
}
