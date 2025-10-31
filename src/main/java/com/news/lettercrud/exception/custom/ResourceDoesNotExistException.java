package com.news.lettercrud.exception.custom;

public class ResourceDoesNotExistException extends RuntimeException{
    public ResourceDoesNotExistException(String message){super(message);}
}
