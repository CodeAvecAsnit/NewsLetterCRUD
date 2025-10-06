package com.news.lettercrud.exceptions;


public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message){super(message);}
}
