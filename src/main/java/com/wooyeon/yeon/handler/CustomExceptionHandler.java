package com.wooyeon.yeon.handler;

import com.wooyeon.yeon.exception.ErrorResponseEntity;
import com.wooyeon.yeon.exception.WooyeonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(WooyeonException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(WooyeonException e) {
        return ErrorResponseEntity.toResponseEntity(e.getExceptionCode());
    }
}
