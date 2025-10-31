package com.news.lettercrud.exception.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){super("The requested user was not found");}
}
