package com.wooyeon.yeon.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private String resultCode;
    private String description;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ExceptionCode e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseEntity.builder()
                        .resultCode(e.getResultCode())
                        .description(e.getDescription())
                        .build()
                );
    }
}
