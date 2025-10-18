package com.news.lettercrud.exceptions.custom;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String s) {super(s);
    }
}
