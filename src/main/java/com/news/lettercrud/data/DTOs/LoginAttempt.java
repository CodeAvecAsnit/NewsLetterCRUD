package com.news.lettercrud.data.DTOs;

import com.news.lettercrud.exception.custom.OutOfTriesException;


public class LoginAttempt {
    private int loginAttempts;
    private int code;
    private int mailsSent;



    public LoginAttempt(int code){
        this.code = code;
        this.loginAttempts = 5;
        this.mailsSent=1;
    }

    public int equals(Integer emailCode){
        if(loginAttempts<=0){
            throw new OutOfTriesException("Max Limit Reached. Try again later");
        }
        --loginAttempts;
        return (code==emailCode)? 1 : 0;
    }

    public void setNewCode(int code){
        this.code = code;
    }

    public boolean canSendMail(){
        return this.mailsSent++>5;
    }
}
