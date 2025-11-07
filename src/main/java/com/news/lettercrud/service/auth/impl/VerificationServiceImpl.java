package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.dto.LoginAttempt;
import com.news.lettercrud.data.dto.ResultDTO;
import com.news.lettercrud.data.enumeration.VerificationResult;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.exception.custom.OutOfTriesException;
import com.news.lettercrud.exception.custom.ResourceDoesNotExistException;
import com.news.lettercrud.exception.custom.UnknownException;
import com.news.lettercrud.exception.custom.UserNotFoundException;
import com.news.lettercrud.service.components.PendingAccountRepository;
import com.news.lettercrud.service.components.VerificationCodeRepository;
import com.news.lettercrud.security.JwtUtils;
import com.news.lettercrud.security.UserDetailsImpl;
import com.news.lettercrud.service.auth.AccountPersister;
import com.news.lettercrud.service.auth.VerificationService;
import com.news.lettercrud.service.infrastructure.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author : Asnit Bakhati
 */

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    private final VerificationCodeRepository codeRepository;
    private final PendingAccountRepository accountRepository;
    private final SecureRandom secureRandom;
    private final NotificationService notificationService;
    private final AccountPersister accountPersister;
    private final JwtUtils jwtUtils;

    @Autowired
    public VerificationServiceImpl(
            @Qualifier("redisCodeRepository") VerificationCodeRepository codeRepository,
            @Qualifier("redisPendingAccountRepository") PendingAccountRepository accountRepository,
            SecureRandom secureRandom,
            @Qualifier("mailServiceImpl") NotificationService notificationService,
            AccountPersister accountPersister, JwtUtils jwtUtils
    ) {
        this.codeRepository = codeRepository;
        this.accountRepository = accountRepository;
        this.secureRandom = secureRandom;
        this.notificationService = notificationService;
        this.accountPersister = accountPersister;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Stores account info in cache and also sends the verification mail
     */
    @Override
    public void sendVerificationCode(BaseAccount account) {
        String email = account.getEmail();

        if (codeRepository.exists(email)) {
            logger.info("Verification code already sent for email: {}", email);
            return;
        }
        int code = secureRandom.nextInt(100000, 1000000);

        try {

            LoginAttempt attempt = new LoginAttempt(code);
            codeRepository.store(email, attempt);
            accountRepository.store(email, account);

            CompletableFuture.runAsync(()->notificationService.sendMail(email,code));

            logger.info("Verification code sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification code to: {}", email, e);
        }
    }

    /**
     * Matches the verification result and persists the account
     */
    @Override
    public ResultDTO verify(String email, int code) {
        Optional<LoginAttempt> attemptOpt = codeRepository.find(email);
        if (attemptOpt.isEmpty()) {
            return new ResultDTO(VerificationResult.CODE_EXPIRED);
        }

        LoginAttempt attempt = attemptOpt.get();
        int validationResult = attempt.equals(code);

        if (validationResult == 1) {
            return handleSuccessfulVerification(email);
        } else {
            return mapAttemptResultToVerificationResult(validationResult);
        }
    }


    @Override
    public void resendVerificationCode(String email) {
        Optional<LoginAttempt> attempt = codeRepository.find(email);
        if(attempt.isEmpty()){
            throw new ResourceDoesNotExistException("Details not found.Please sign up again");
        }
        LoginAttempt att = attempt.get();
        if(!att.canSendMail()){
            throw new OutOfTriesException("Cannot send code. Try again later.");
        }
        Optional<BaseAccount> account = accountRepository.find(email);
        if(account.isEmpty()){
            throw new UserNotFoundException();
        }
        int newCode = secureRandom.nextInt(100000, 1000000);
        try {
            att.setNewCode(newCode);
            codeRepository.remove(email);
            codeRepository.store(email, att);
            accountRepository.remove(email);
            accountRepository.store(email, account.get());
            CompletableFuture.runAsync(() -> notificationService.sendMail(email, newCode));
        }catch (Exception ex){
            throw new UnknownException();
        }
    }


    /**
     * Persists the account if verification is successful
     */
    private ResultDTO handleSuccessfulVerification(String email) {
        codeRepository.remove(email);

        Optional<BaseAccount> accountOpt = accountRepository.find(email);
        if (accountOpt.isEmpty()) {
            return new ResultDTO(VerificationResult.ACCOUNT_NOT_FOUND);
        }

        BaseAccount account = accountOpt.get();
        accountRepository.remove(email);

        try {
            BaseAccount acc = accountPersister.persist(account);
            String jwt = jwtUtils.generateJwtTokens(UserDetailsImpl.build(acc));
            logger.info("Account verified and persisted successfully: {}", email);
            return new ResultDTO(VerificationResult.SUCCESS,jwt);
        } catch (Exception e) {
            logger.error("Failed to persist account: {}", email, e);
            throw new RuntimeException("Failed to persist account", e);
        }
    }

    /**
     *  Maps the verification Result to different result states
     */
    private ResultDTO mapAttemptResultToVerificationResult(int code) {
        return switch (code) {
            case 2 -> new ResultDTO(VerificationResult.CODE_MISMATCH);
            case 3 -> new ResultDTO(VerificationResult.TOO_MANY_ATTEMPTS);
            default ->new ResultDTO(VerificationResult.CODE_MISMATCH);
        };
    }
}