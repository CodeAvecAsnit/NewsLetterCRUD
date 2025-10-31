package com.news.lettercrud.service.auth;

import com.news.lettercrud.data.DTOs.LoginDTO;
import com.news.lettercrud.data.DTOs.LoginResponseDT0;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public interface LoginService {

    LoginResponseDT0 login(LoginDTO request, HttpServletResponse response, HttpServletRequest httpRequest);
}
