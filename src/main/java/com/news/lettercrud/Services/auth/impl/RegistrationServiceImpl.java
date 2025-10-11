package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.Data.DTOs.RegistrationDTO;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Services.auth.RegistrationService;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.exceptions.custom.EmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final UserService userService;

    @Autowired
    public RegistrationServiceImpl(
            @Qualifier("userServiceImpl")UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        boolean userExists = userService.existsByEmail(email);
        return !userExists;
    }

    private void createAccount(String email) {
        if (!isEmailAvailable(email)) {
            throw new EmailAlreadyExistsException("Email already registered: " + email);
        }
    }

    @Override
    public CompanyAccount createCompanyAccount(CompanyRegistrationDTO dto) {
        createAccount(dto.getEmail());
        return CompanyRegistrationDTO.buildCompany(dto);
    }

    @Override
    public UserAccount createUserAccount(RegistrationDTO dto) {
        createAccount(dto.getEmail());
        return RegistrationDTO.buildUser(dto);
    }
}