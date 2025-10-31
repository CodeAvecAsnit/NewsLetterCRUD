package com.news.lettercrud.exception.custom;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String s) {super(s);
    }
}
