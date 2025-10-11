package com.news.lettercrud.exceptions.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){super("The requested user was not found");}
}
