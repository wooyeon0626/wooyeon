package com.wooyeon.yeon.exception;

public class WooyeonException extends Exception{

    public ExceptionMessage exceptionMessage;

    public WooyeonException(ExceptionMessage exceptionMessage){
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }
}
