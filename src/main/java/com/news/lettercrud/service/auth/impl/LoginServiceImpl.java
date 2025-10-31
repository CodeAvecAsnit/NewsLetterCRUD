package com.news.lettercrud.service.auth.impl;

import com.news.lettercrud.data.DTOs.LoginDTO;
import com.news.lettercrud.data.DTOs.LoginResponseDT0;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.RefreshToken;
import com.news.lettercrud.repository.BaseAccountRepository;
import com.news.lettercrud.security.JwtUtils;
import com.news.lettercrud.security.RefreshTokenService;
import com.news.lettercrud.security.UserDetailsImpl;
import com.news.lettercrud.security.UserDetailsImplService;
import com.news.lettercrud.service.auth.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */

@Service
public class LoginServiceImpl implements LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final BaseAccountRepository baseAccountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public LoginServiceImpl(UserDetailsImplService userDetailsImplService, BaseAccountRepository baseAccountRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.baseAccountRepository = baseAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }


    /**
     *
     * @param logUser contains email and password
     * @return true if password matches false if password doesn't match
     */

    private boolean checkPassword(LoginDTO logUser,String password) {
        return passwordEncoder.matches(logUser.getPassword(), password);
    }



    /**
     * this function used to initiate log in a user and create a session
     * @param loginData contains email and password required for authorization
     * @param httpResponse is used to attach jwt token in http only cookie
     * @return responseCode, Jwt token(not safe) , message
     */

    @Override
    public LoginResponseDT0 login(LoginDTO loginData, HttpServletResponse httpResponse, HttpServletRequest request) {
        //TODO : Implement a Password rate limiting
        try {
            BaseAccount baseAccount = baseAccountRepository.findByEmail(loginData.getEmail());
            if (baseAccount == null) {
                return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
            }
            boolean matcher = checkPassword(loginData,baseAccount.getPassword());
            if (matcher) {
                UserDetailsImpl user = UserDetailsImpl.build(baseAccount);
               String token = jwtUtils.generateJwtTokens(user);
               RefreshToken refreshToken = refreshTokenService.createRefreshToken(baseAccount,getDeviceInfo(request));
               attachRefreshToken(httpResponse,refreshToken.getToken());
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
        cookie.setSecure(false);//set true in production through https
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }


    private void attachRefreshToken(HttpServletResponse response,String refreshToken){
        Cookie cookie = new Cookie("refresh_token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//set true in production through https
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }


    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown Device";
    }

}