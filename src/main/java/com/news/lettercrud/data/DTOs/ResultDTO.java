package com.news.lettercrud.data.DTOs;

import com.news.lettercrud.data.Enum.VerificationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResultDTO {
    private VerificationResult verificationResult;
    private String token;


    public ResultDTO(VerificationResult verificationResult) {
        this.token="";
        this.verificationResult = verificationResult;
    }

}
