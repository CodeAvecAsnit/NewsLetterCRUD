package com.news.lettercrud.exceptions;

import com.news.lettercrud.exceptions.custom.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNoUser(UserNotFoundException e){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).
                body(Map.of("error",e.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleEmail(EmailAlreadyExistsException e){
        return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNews(NewsNotFoundException e){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<Map<String,String>> handleError(UnknownException e){
        return ResponseEntity.internalServerError().body(Map.of("error","Something unexpected occured"));
    }

    @ExceptionHandler(WrongAuthorException.class)
    public ResponseEntity<Map<String,String>> handleUnauthorizedAccess(WrongAuthorException e){
        return ResponseEntity.status(401).body(Map.of("error","unauthorized"));
    }


}
