package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.CompanyAccount;
import com.news.lettercrud.data.model.UserAccount;
import com.news.lettercrud.repository.CompanyRepository;
import com.news.lettercrud.repository.UserAccountRepository;
import com.news.lettercrud.service.auth.AccountPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */
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


    /**
     * saves the account information as UserAccount or Company Account
     */
    @Override
    public BaseAccount persist(BaseAccount account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == Role.COMPANY) {
            return (BaseAccount) companyRepository.save((CompanyAccount) account);
        } else {
            return (BaseAccount) userAccountRepository.save((UserAccount) account);
        }
    }
}