package com.news.lettercrud.Services.auth.impl;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;
import com.news.lettercrud.Security.JwtUtils;
import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Security.UserDetailsImplService;
import com.news.lettercrud.Services.auth.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */

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


    /**
     *
     * @param logUser contains email and password
     * @return true if password matches false if password doesn't match
     */

    private boolean checkPassword(LoginDTO logUser) {
        UserDetails user = userDetailsImplService.loadUserByUsername(logUser.getEmail());
        return passwordEncoder.matches(logUser.getPassword(), user.getPassword());
    }



    /**
     * this function used to initiate log in a user and create a session
     * @param loginData contains email and password required for authorization
     * @param httpResponse is used to attach jwt token in http only cookie
     * @return responseCode, Jwt token(not safe) , message
     */

    @Override
    public LoginResponseDT0 login(LoginDTO loginData, HttpServletResponse httpResponse) {
        //TODO : Implement a Password rate limiting
        try {
            boolean matcher = checkPassword(loginData);
            if (matcher) {
               String token = jwtUtils.generateJwtTokens((UserDetailsImpl)userDetailsImplService.loadUserByUsername(loginData.getEmail()));
               attachJwt(httpResponse,token);
                return new LoginResponseDT0(200, token, "Success");
            } else return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        }
    }



    private void attachJwt(HttpServletResponse response,String jwt){
        Cookie cookie = new Cookie("access_token",jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }

}