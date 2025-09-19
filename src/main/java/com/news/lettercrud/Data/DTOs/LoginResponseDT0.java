package com.news.lettercrud.Data.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for Login Request")
public class LoginResponseDT0{

    private int responseCode;
    private String token;
    private String message;


    public LoginResponseDT0(){}

    public LoginResponseDT0(int responseCode,String token, String message) {
        this.responseCode = responseCode;
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
