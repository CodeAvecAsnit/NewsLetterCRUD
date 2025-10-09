package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;
import com.news.lettercrud.Security.JwtUtils;
import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Security.UserDetailsImplService;
import com.news.lettercrud.Services.auth.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class LoginServiceImpl implements LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final UserDetailsImplService userDetailsImplService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    @Autowired
    public LoginServiceImpl(UserDetailsImplService userDetailsImplService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userDetailsImplService = userDetailsImplService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    private boolean checkPassword(LoginDTO logUser) {
        UserDetails user = userDetailsImplService.loadUserByUsername(logUser.getEmail());
        return passwordEncoder.matches(logUser.getPassword(), user.getPassword());
    }

    @Override
    public LoginResponseDT0 login(LoginDTO request) {
        //TODO : Implement a Password rate limiting
        try {
            boolean matcher = checkPassword(request);
            if (matcher) {
               String token = jwtUtils.generateJwtTokens((UserDetailsImpl)userDetailsImplService.loadUserByUsername(request.getEmail()));
                return new LoginResponseDT0(200, "token", "Success");
            } else return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        }
    }

}