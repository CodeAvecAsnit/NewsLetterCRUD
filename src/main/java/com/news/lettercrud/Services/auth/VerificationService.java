package com.news.lettercrud.Services.auth;

import com.news.lettercrud.Data.Enum.VerificationResult;
import com.news.lettercrud.Data.model.BaseAccount;
import org.springframework.scheduling.annotation.Async;

public interface VerificationService {

    @Async
    void sendVerificationCode(BaseAccount account);

    VerificationResult verify(String email, int code);
}
