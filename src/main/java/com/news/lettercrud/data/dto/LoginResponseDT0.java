package com.news.lettercrud.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
