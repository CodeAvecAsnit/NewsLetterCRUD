package com.news.lettercrud.data.DTOs;

import com.news.lettercrud.exception.custom.OutOfTriesException;

//Internal DTO
public class LoginAttempt {
    private int loginAttempts;
    private final int code;



    public LoginAttempt(int code){
        this.code = code;
        this.loginAttempts = 5;
    }

    public int equals(Integer emailCode){
        if(loginAttempts<=0){
            throw new OutOfTriesException("Max Limit Reached. Try again later");
        }
        --loginAttempts;
        return (code==emailCode)? 1 : 0;
    }
}
