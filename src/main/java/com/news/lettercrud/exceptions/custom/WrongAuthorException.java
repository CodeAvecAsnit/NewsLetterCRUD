package com.news.lettercrud.exceptions.custom;

public class WrongAuthorException extends RuntimeException{
    public WrongAuthorException(){super("Sorry you dont have access to delete this");};
}
