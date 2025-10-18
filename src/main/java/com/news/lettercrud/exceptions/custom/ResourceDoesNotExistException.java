package com.news.lettercrud.exceptions.custom;

public class ResourceDoesNotExistException extends RuntimeException{
    public ResourceDoesNotExistException(String message){super(message);}
}
