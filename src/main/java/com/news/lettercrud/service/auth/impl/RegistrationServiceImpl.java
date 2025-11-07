package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.dto.CompanyRegistrationDTO;
import com.news.lettercrud.data.dto.RegistrationDTO;
import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.CompanyAccount;
import com.news.lettercrud.data.model.RoleTable;
import com.news.lettercrud.data.model.UserAccount;
import com.news.lettercrud.repository.RoleTableRepository;
import com.news.lettercrud.service.auth.RegistrationService;
import com.news.lettercrud.service.model.UserService;
import com.news.lettercrud.exception.custom.EmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private final RoleTableRepository roleTableRepository;

    private final UserService userService;

    @Autowired
    public RegistrationServiceImpl(
            RoleTableRepository roleTableRepository, @Qualifier("userServiceImpl")UserService userService) {
        this.roleTableRepository = roleTableRepository;
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
        RoleTable roleTable = roleTableRepository.findByRole(Role.COMPANY);
        List<RoleTable> roleTableList = new ArrayList<>();
        roleTableList.add(roleTable);
        return CompanyRegistrationDTO.buildCompany(dto,roleTableList);
    }

    @Override
    public UserAccount createUserAccount(RegistrationDTO dto) {
        createAccount(dto.getEmail());
        RoleTable roleTable = roleTableRepository.findByRole(Role.USER);
        List<RoleTable> roleTableList = new ArrayList<>();
        roleTableList.add(roleTable);
        return RegistrationDTO.buildUser(dto,roleTableList);
    }
}