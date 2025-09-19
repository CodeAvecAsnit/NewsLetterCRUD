package com.news.lettercrud.Data.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema (description = "Sending mail with verification")
public class MailVerificationDTO {
    private String email;
    private int code;

    public MailVerificationDTO() {
    }

    public MailVerificationDTO(String email, Integer code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
