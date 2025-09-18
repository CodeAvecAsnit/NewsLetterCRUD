package com.news.lettercrud.Data.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Response")
public class APIResponseDTO {

    @Schema(description = "Status message", example = "Successfully Registered")
    private String message;

    @Schema(description = "Optional JWT token")
    private String token;

    public APIResponseDTO() {}

    public APIResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}