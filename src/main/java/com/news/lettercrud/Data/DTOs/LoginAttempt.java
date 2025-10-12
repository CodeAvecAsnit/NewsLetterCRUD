package com.news.lettercrud.Data.DTOs;

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
            return -1;
        }
        --loginAttempts;
        return (code==emailCode)? 1 : 0;
    }
}
