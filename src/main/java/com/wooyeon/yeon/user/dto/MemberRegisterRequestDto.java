package com.wooyeon.yeon.user.dto;

public class MemberRegisterRequestDto {

    private String email;
    private String password;

    // 생성자, 게터, 세터 생략 (Lombok을 사용하면 자동으로 생성)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
