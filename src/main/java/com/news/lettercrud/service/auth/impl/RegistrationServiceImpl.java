package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.data.DTOs.RegistrationDTO;
import com.news.lettercrud.data.model.CompanyAccount;
import com.news.lettercrud.data.model.UserAccount;
import com.news.lettercrud.service.auth.RegistrationService;
import com.news.lettercrud.service.model.UserService;
import com.news.lettercrud.exception.custom.EmailAlreadyExistsException;
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

    /**
     * Checks if email is already registers and throws Error if found
     */
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