package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.LoginAttempt;
import com.news.lettercrud.Data.DTOs.VerificationCode;
import com.news.lettercrud.Data.Enum.VerificationResult;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.PendingAccountRepository;
import com.news.lettercrud.Repositories.VerificationCodeRepository;
import com.news.lettercrud.Services.auth.AccountPersister;
import com.news.lettercrud.Services.auth.VerificationCodeGenerator;
import com.news.lettercrud.Services.auth.VerificationService;
import com.news.lettercrud.Services.infrastructure.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    private final VerificationCodeRepository codeRepository;
    private final PendingAccountRepository accountRepository;
    private final VerificationCodeGenerator codeGenerator;
    private final NotificationService notificationService;
    private final AccountPersister accountPersister;

    @Autowired
    public VerificationServiceImpl(
            VerificationCodeRepository codeRepository,
            PendingAccountRepository accountRepository,
            VerificationCodeGenerator codeGenerator,
            @Qualifier("mailServiceImpl") NotificationService notificationService,
            AccountPersister accountPersister
    ) {
        this.codeRepository = codeRepository;
        this.accountRepository = accountRepository;
        this.codeGenerator = codeGenerator;
        this.notificationService = notificationService;
        this.accountPersister = accountPersister;
    }

    @Override
    @Async
    public void sendVerificationCode(BaseAccount account) {
        String email = account.getEmail();

        if (codeRepository.exists(email)) {
            logger.info("Verification code already sent for email: {}", email);
            return;
        }

        VerificationCode code = codeGenerator.generate();

        try {
            notificationService.sendMail(email, code.getValue());

            LoginAttempt attempt = new LoginAttempt(code.getValue());
            codeRepository.store(email, attempt);
            accountRepository.store(email, account);

            logger.info("Verification code sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification code to: {}", email, e);
        }
    }

    @Override
    public VerificationResult verify(String email, int code) {
        Optional<LoginAttempt> attemptOpt = codeRepository.find(email);
        if (attemptOpt.isEmpty()) {
            return VerificationResult.CODE_EXPIRED;
        }

        LoginAttempt attempt = attemptOpt.get();
        int validationResult = attempt.equals(code);

        if (validationResult == 1) {
            return handleSuccessfulVerification(email);
        } else {
            return mapAttemptResultToVerificationResult(validationResult);
        }
    }

    private VerificationResult handleSuccessfulVerification(String email) {
        codeRepository.remove(email);

        Optional<BaseAccount> accountOpt = accountRepository.find(email);
        if (accountOpt.isEmpty()) {
            return VerificationResult.ACCOUNT_NOT_FOUND;
        }

        BaseAccount account = accountOpt.get();
        accountRepository.remove(email);

        try {
            accountPersister.persist(account);
            logger.info("Account verified and persisted successfully: {}", email);
            return VerificationResult.SUCCESS;
        } catch (Exception e) {
            logger.error("Failed to persist account: {}", email, e);
            throw new RuntimeException("Failed to persist account", e);
        }
    }

    private VerificationResult mapAttemptResultToVerificationResult(int code) {
        return switch (code) {
            case 2 -> VerificationResult.CODE_MISMATCH;
            case 3 -> VerificationResult.TOO_MANY_ATTEMPTS;
            default -> VerificationResult.CODE_MISMATCH;
        };
    }
}