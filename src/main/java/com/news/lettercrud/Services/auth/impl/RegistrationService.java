package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.Data.DTOs.RegistrationDTO;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.CompanyRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import com.news.lettercrud.exceptions.EmailAlreadyExistsException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final UserAccountRepository userAccountRepository;
    private final CompanyRepository companyRepository;
    private final EmailValidator emailValidator;

    @Autowired
    public RegistrationService(
            UserAccountRepository userAccountRepository,
            CompanyRepository companyRepository,
            EmailValidator emailValidator) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.emailValidator = emailValidator;
    }

    public boolean isEmailAvailable(String email) {
        boolean userExists = userAccountRepository.existsByEmail(email);
        boolean companyExists = companyRepository.existsByEmail(email);
        return !userExists && !companyExists;
    }

    private void createAccount(String email) {
        if (!isEmailAvailable(email)) {
            throw new EmailAlreadyExistsException("Email already registered: " + email);
        }
    }

    public CompanyAccount createCompanyAccount(CompanyRegistrationDTO dto) {
        createAccount(dto.getEmail());
        return CompanyRegistrationDTO.buildCompany(dto);
    }

    public UserAccount createUserAccount(RegistrationDTO dto) {
        createAccount(dto.getEmail());
        return RegistrationDTO.buildUser(dto);
    }
}