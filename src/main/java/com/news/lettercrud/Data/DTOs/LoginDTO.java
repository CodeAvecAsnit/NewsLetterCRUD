package com.news.lettercrud.Data.DTOs;

import com.news.lettercrud.Data.Enum.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "email and password for authentication")
public class LoginDTO {
    private String email;
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String password, String email, Role role) {
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
