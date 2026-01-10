package com.banking.demo.controller;

import com.banking.demo.config.JwtUtil;
import com.banking.demo.dto.LoginRequest;
import com.banking.demo.dto.LoginResponse;
import com.banking.demo.dto.RegisterRequest;
import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(
            UserRepository userRepo,
            PasswordEncoder encoder,
            JwtUtil jwtUtil
    ) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(token, user.getUsername(), user.getRole().name());
    }

    // ---------------- REGISTER ----------------
    // Used by ADMIN (via header token)
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        // ✅ STRING → ENUM (THIS FIXES YOUR ERROR)
        user.setRole(User.Role.valueOf(request.getRole()));

        user.setActive(true);

        return userRepo.save(user);
    }
}
