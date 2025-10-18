package com.news.lettercrud.Data.DTOs;

import com.news.lettercrud.Data.Enum.VerificationResult;


public class ResultDTO {
    private VerificationResult verificationResult;
    private String token;

    public ResultDTO(){}

    public ResultDTO(VerificationResult verificationResult) {
        this.token="";
        this.verificationResult = verificationResult;
    }

    public ResultDTO(VerificationResult verificationResult, String token) {
        this.verificationResult = verificationResult;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public VerificationResult getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(VerificationResult verificationResult) {
        this.verificationResult = verificationResult;
    }
}
