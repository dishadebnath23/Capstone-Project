package com.banking.demo.controller;

import jakarta.validation.constraints.NotBlank;

class DummyRequest {

    @NotBlank(message = "name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
