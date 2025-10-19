package com.news.lettercrud.Services.auth;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public interface LoginService {

    LoginResponseDT0 login(LoginDTO request, HttpServletResponse response, HttpServletRequest httpRequest);
}
