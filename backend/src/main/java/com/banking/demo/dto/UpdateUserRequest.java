package com.banking.demo.dto;

public class UpdateUserRequest {

    private String email;
    private String role; // RM or ANALYST

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
