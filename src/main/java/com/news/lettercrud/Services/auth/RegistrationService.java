package com.news.lettercrud.Services.auth;

import com.news.lettercrud.Data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.Data.DTOs.RegistrationDTO;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;

public interface RegistrationService {
    boolean isEmailAvailable(String email);

    CompanyAccount createCompanyAccount(CompanyRegistrationDTO dto);

    UserAccount createUserAccount(RegistrationDTO dto);
}
