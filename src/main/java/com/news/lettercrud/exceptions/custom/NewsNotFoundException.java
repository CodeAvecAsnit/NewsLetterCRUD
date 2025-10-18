package com.news.lettercrud.exceptions.custom;


public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message){super(message);}
}
