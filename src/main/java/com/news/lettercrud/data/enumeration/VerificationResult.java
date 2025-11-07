package com.news.lettercrud.data.enumeration;


public enum VerificationResult {
    SUCCESS(1, "Verification successful"),
    CODE_EXPIRED(5, "Verification code expired or not found"),
    ACCOUNT_NOT_FOUND(6, "Account not found"),
    CODE_MISMATCH(2, "Invalid verification code"),
    TOO_MANY_ATTEMPTS(3, "Too many failed attempts");

    private final int code;
    private final String message;

    VerificationResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}