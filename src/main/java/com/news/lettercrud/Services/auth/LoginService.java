package com.news.lettercrud.Services.auth;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;

public interface LoginService {

    LoginResponseDT0 login(LoginDTO request);
}
