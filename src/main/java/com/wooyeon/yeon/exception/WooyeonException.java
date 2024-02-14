package com.wooyeon.yeon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WooyeonException extends RuntimeException{
    ExceptionCode exceptionCode;
}
