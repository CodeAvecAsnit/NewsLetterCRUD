package com.news.lettercrud.exception.custom;

public class OutOfTriesException extends RuntimeException {
    public OutOfTriesException(String message){
        super(message);
    }
}
