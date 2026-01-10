package com.banking.demo.controller;

import com.banking.demo.exception.BadRequestException;
import com.banking.demo.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {

    @GetMapping("/test/bad-request")
    public void throwBadRequest() {
        throw new BadRequestException("Bad request");
    }

    @GetMapping("/test/not-found")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Resource not found");
    }

    // ✅ THIS WAS MISSING
    @GetMapping("/test/runtime")
    public void throwRuntime() {
        throw new RuntimeException("Something went wrong");
    }
}
