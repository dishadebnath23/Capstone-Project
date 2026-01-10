package com.banking.demo.dto;

public class UserResponseDto {

    private String id;
    private String username;
    private String role;
    private boolean active;

    // constructor
    public UserResponseDto(String id, String username, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
    }

    // getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
}
