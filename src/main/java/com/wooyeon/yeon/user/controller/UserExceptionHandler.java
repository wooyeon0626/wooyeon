package com.wooyeon.yeon.user.controller;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentHandler(IllegalArgumentException e) {
        return new ResponseEntity<>("statusCode : "+HttpStatus.BAD_REQUEST+"\n문제 : "+e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnsupportedEncodingException.class)
    public ResponseEntity<String> UnsupportedEncodingHandler(UnsupportedEncodingException e) {
        return new ResponseEntity<>("statusCode : "+HttpStatus.BAD_REQUEST+"\n문제 : "+e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> IoExceptionHandler(IOException e) {
        return new ResponseEntity<>("statusCode : "+HttpStatus.BAD_REQUEST+"\n문제 : "+e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidKeyException.class)
    public ResponseEntity<String> InvalidKeyExceptionHandler(InvalidKeyException e) {
        return new ResponseEntity<>("statusCode : "+HttpStatus.BAD_REQUEST+"\n문제 : "+e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpServerErrorException.class)
    public ResponseEntity<String> HttpServletHandler(HttpServerErrorException e) {
        return new ResponseEntity<>("statusCode : "+HttpStatus.BAD_REQUEST+"\n문제 : "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
