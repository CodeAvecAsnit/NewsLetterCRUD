package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.Data.DTOs.MailVerificationDTO;
import com.news.lettercrud.Data.DTOs.RegistrationDTO;
import com.news.lettercrud.Data.DTOs.ResultDTO;
import com.news.lettercrud.Data.Enum.VerificationResult;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Services.auth.RegistrationService;
import com.news.lettercrud.Services.auth.VerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.*;

/**
 * @author : Asnit Bakhati
 */

@Service
public class AccountRegistrationFacade {

    private static final Logger logger = getLogger(AccountRegistrationFacade.class);

    private final RegistrationService registrationService;
    private final VerificationService verificationService;

    @Autowired
    public AccountRegistrationFacade(
            @Qualifier("registrationServiceImpl") RegistrationService registrationService,
            @Qualifier("verificationServiceImpl") VerificationService verificationService
    ) {
        this.registrationService = registrationService;
        this.verificationService = verificationService;
    }

    /**
     * Initiates registration process by creating account and sending verification code
     */
    public void registerUserAccount(RegistrationDTO dto) {
        logger.info("Starting registration process for: {}", dto.getEmail());

        BaseAccount account = (BaseAccount) registrationService.createUserAccount(dto);
        verificationService.sendVerificationCode(account);

    }

    /**
     * Initiates registration process by creating account and sending verification code
     */
    public void registerCompanyAccount(CompanyRegistrationDTO dto) {

        BaseAccount account =(BaseAccount) registrationService.createCompanyAccount(dto);
        verificationService.sendVerificationCode(account);

        logger.info("Registration initiated successfully for: {}", dto.getEmail());
    }

    /**
     * Verifies the code and completes registration
     */
    public ResultDTO verifyAndCompleteRegistration(MailVerificationDTO dto) {
        logger.info("Verifying registration for: {}", dto.getEmail());

        ResultDTO result = verificationService.verify(dto.getEmail(), dto.getCode());

        if (result.getVerificationResult() == VerificationResult.SUCCESS) {
            logger.info("Registration completed successfully for: {}", dto.getEmail());
        } else {
            logger.warn("Verification failed for: {} - Reason: {}", dto.getEmail());
        }

        return result;
    }

    /**
     * Checks if email is available for registration
     */
    public boolean isEmailAvailable(String email) {
        return registrationService.isEmailAvailable(email);
    }
    /**
     * Delete the JWTe
     */

    public void expireCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
