package com.banking.demo.dto;

public class LoginRequest {

    private String username;
    private String password;

    // REQUIRED: no-args constructor
    public LoginRequest() {
    }

    // getters & setters
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

