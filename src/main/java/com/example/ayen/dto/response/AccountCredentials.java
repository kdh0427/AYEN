package com.example.ayen.dto.response;

public class AccountCredentials {
    private String username;
    private String password;

    // 기본 생성자
    public AccountCredentials() {}

    // 생성자
    public AccountCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 게터·세터
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
