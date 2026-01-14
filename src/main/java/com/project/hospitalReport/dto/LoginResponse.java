package com.project.hospitalReport.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long userId;
    private String name;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String name) {
        this.token = token;
        this.userId = userId;
        this.name = name;
    }
}

