package com.news.lettercrud.service.auth;

import com.news.lettercrud.data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.data.DTOs.RegistrationDTO;
import com.news.lettercrud.data.model.CompanyAccount;
import com.news.lettercrud.data.model.UserAccount;

public interface RegistrationService {
    boolean isEmailAvailable(String email);

    CompanyAccount createCompanyAccount(CompanyRegistrationDTO dto);

    UserAccount createUserAccount(RegistrationDTO dto);
}
