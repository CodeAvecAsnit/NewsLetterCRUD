package com.news.lettercrud.exception.custom;


public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message){super(message);}
}
