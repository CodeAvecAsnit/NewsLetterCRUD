package com.news.lettercrud.data.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "email and password for authentication")
public class LoginDTO {
    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }

}
