package com.news.lettercrud.service.auth;

import com.news.lettercrud.data.dto.ResultDTO;
import com.news.lettercrud.data.model.BaseAccount;
import org.springframework.scheduling.annotation.Async;

public interface VerificationService {

    @Async
    void sendVerificationCode(BaseAccount account);

    ResultDTO verify(String email, int code);

    void resendVerificationCode(String email);
}
