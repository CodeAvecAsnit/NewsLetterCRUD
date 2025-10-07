package com.news.lettercrud.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){super("The requested user was not found");}
}
