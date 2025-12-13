package com.project.hospitalReport.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private String name;
    private String message;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String name, String message) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

