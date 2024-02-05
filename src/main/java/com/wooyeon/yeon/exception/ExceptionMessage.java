package com.wooyeon.yeon.exception;

public enum ExceptionMessage {
    AUTHENTICATION_FAILED("인증 실패"),
    AUTHORIZATION_FAILED("접근 권한 없음"),
    USER_NOT_FOUND("DB에 유저가 존재하지 않습니다"),
    USER_MATCH_NOT_FOUND("DB에 매치 정보가 존재하지 않습니다"),
    FCM_SEND_FAIL_ERROR("FCM 메시지 전송에 실패"),
    LOGIN_USER_NOT_FOUND("DB에 로그인된 유저 정보가 존재하지 않습니다");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}