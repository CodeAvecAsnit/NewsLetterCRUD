package com.news.lettercrud.Data.DTOs;

//Internal Class flow
public class VerificationCode {

        private final int code;

        public VerificationCode(int code) {
            if (code < 100000 || code >= 1000000) {
                throw new IllegalArgumentException("Invalid verification code");
            }
            this.code = code;
        }

        public int getValue() {
            return code;
        }

        public boolean matches(int otherCode) {
            return this.code == otherCode;
        }

}
