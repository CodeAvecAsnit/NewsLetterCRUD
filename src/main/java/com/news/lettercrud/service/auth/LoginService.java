package com.news.lettercrud.service.auth;

import com.news.lettercrud.data.dto.LoginDTO;
import com.news.lettercrud.data.dto.LoginResponseDT0;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public interface LoginService {
    LoginResponseDT0 login(LoginDTO request, HttpServletResponse response, HttpServletRequest httpRequest);
}
