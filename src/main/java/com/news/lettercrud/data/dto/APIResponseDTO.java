package com.news.lettercrud.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "API Response")
public class APIResponseDTO {

    // getters and setters
    @Schema(description = "Status message", example = "Successfully Registered")
    private String message;

    @Schema(description = "Optional JWT token")
    private String token;

    public APIResponseDTO() {}

    public APIResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

}