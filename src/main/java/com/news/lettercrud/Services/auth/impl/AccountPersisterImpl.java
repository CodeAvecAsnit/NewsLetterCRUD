package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.CompanyRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import com.news.lettercrud.Services.auth.AccountPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountPersisterImpl implements AccountPersister {
    private final UserAccountRepository userAccountRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountPersisterImpl(
            UserAccountRepository userAccountRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void persist(BaseAccount account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == Role.COMPANY) {
            companyRepository.save((CompanyAccount) account);
        } else {
            userAccountRepository.save((UserAccount) account);
        }
    }
}