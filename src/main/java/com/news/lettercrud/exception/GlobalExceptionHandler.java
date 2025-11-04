package com.news.lettercrud.exception;

import com.news.lettercrud.exception.custom.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * @author : Asnit Bakhati
 */

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
        return ResponseEntity.status(404).body(Map.of("Error","Requested news not found"));
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<Map<String,String>> handleError(UnknownException e){
        return ResponseEntity.internalServerError().body(Map.of("error","Something unexpected occurred"));
    }

    @ExceptionHandler(WrongAuthorException.class)
    public ResponseEntity<Map<String,String>> handleUnauthorizedAccess(WrongAuthorException e){
        return ResponseEntity.status(401).body(Map.of("error","unauthorized"));
    }

    @ExceptionHandler(ResourceDoesNotExistException.class)
    public ResponseEntity<Map<String,String>> handleNotfoundResource(ResourceDoesNotExistException rEx){
        return ResponseEntity.status(404).body(Map.of("error",rEx.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String,String>> handleValidationCases(MethodArgumentNotValidException mEx){
        return ResponseEntity.status(400).body(Map.of("Error","Request is invalid"));
    }

    @ExceptionHandler(OutOfTriesException.class)
    public ResponseEntity<Map<String,String>> handleBruteForce(OutOfTriesException oEx){
        return ResponseEntity.badRequest().body(Map.of("Error",oEx.getMessage()));
    }
}
