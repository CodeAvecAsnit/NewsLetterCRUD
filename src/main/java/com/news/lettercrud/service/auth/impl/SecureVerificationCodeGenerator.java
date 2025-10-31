package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.DTOs.VerificationCode;
import com.news.lettercrud.service.auth.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SecureVerificationCodeGenerator implements VerificationCodeGenerator {

    private final SecureRandom secureRandom;

    @Autowired
    public SecureVerificationCodeGenerator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    /**
     * Generates and Random One Time code send to user in mail for verification
     */
    @Override
    public VerificationCode generate() {
        int code = secureRandom.nextInt(100000, 1000000);
        return new VerificationCode(code);
    }
}
