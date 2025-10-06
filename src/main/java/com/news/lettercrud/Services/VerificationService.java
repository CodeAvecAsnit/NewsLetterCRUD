package com.news.lettercrud.Services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.news.lettercrud.Data.DTOs.LoginAttempt;
import com.news.lettercrud.Data.DTOs.MailVerificationDTO;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.CompanyRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
public class VerificationService{

    private final Logger logger = LoggerFactory.getLogger(VerificationService.class);

    private Cache<String, LoginAttempt> codes;

    private Cache<String, BaseAccount> accounts;

    private final UserAccountRepository userAccountRepository;

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailServiceImpl mailServiceImpl;

    private final SecureRandom secureRandom;


    @Autowired
    public VerificationService(UserAccountRepository userAccountRepository, CompanyRepository companyRepository
                               , PasswordEncoder passwordEncoder, MailServiceImpl mailServiceImpl, SecureRandom secureRandom
    ) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailServiceImpl = mailServiceImpl;
        this.secureRandom = secureRandom;
    }

    @PostConstruct
    public void init(){
        this.codes = Caffeine.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES).
                maximumSize(1000).build();
        this.accounts =Caffeine.newBuilder().
                expireAfterWrite(5,TimeUnit.MINUTES).
                maximumSize(1000).build();
    }

    @Async
    public void sendMail(BaseAccount baseAccount) {
        String email = baseAccount.getEmail();
        LoginAttempt attempt = codes.getIfPresent(email);
        if(attempt!=null){
            return;
        }
        int code =secureRandom.nextInt(100000,1000000);
        mailServiceImpl.sendMail(baseAccount.getEmail(),code);
        if(code != -1){
            codes.put(email,new LoginAttempt(code));
            accounts.put(email,baseAccount);
        }
    }


    public int verify(MailVerificationDTO verify){
        String email = verify.getEmail();
        LoginAttempt validAttempt = codes.getIfPresent(email);
        if(validAttempt==null){
            return 5;
        }
        int returnCode = validAttempt.equals(verify.getCode());
        if(returnCode==1){
            codes.invalidate(email);
            BaseAccount account = accounts.getIfPresent(email);
            if(account==null){
                return 6;
            }
            accounts.invalidate(email);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            if(account.getRole()== Role.COMPANY){
                CompanyAccount companyAccount=(CompanyAccount) account;
                companyRepository.save(companyAccount);
            }else{
                UserAccount userAccount = (UserAccount) account;
                userAccountRepository.save(userAccount);
            }
            return 1;
        }
        else return returnCode;
    }

}