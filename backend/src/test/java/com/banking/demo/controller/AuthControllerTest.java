package com.banking.demo.controller;

import com.banking.demo.config.JwtUtil;
import com.banking.demo.dto.LoginRequest;
import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔑 disable Spring Security
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 🔑 MOCK ALL constructor dependencies
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    // ---------------- LOGIN SUCCESS ----------------
    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("rm_user");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("rm_user");
        user.setPassword("encoded-pass");
        user.setRole(User.Role.RELATIONSHIP_MANAGER);
        user.setActive(true);

        when(userRepository.findByUsername("rm_user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded-pass"))
                .thenReturn(true);
        when(jwtUtil.generateToken("rm_user", "RELATIONSHIP_MANAGER"))
                .thenReturn("jwt-token");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("rm_user"))
                .andExpect(jsonPath("$.role").value("RELATIONSHIP_MANAGER"));
    }

    // ---------------- USER NOT FOUND ----------------
    @Test
    void shouldReturnBadRequestWhenUserNotFound() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("pass");

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    // ---------------- INVALID PASSWORD ----------------
    @Test
    void shouldReturnBadRequestWhenPasswordInvalid() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("rm_user");
        request.setPassword("wrong");

        User user = new User();
        user.setUsername("rm_user");
        user.setPassword("encoded");
        user.setActive(true);

        when(userRepository.findByUsername("rm_user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded"))
                .thenReturn(false);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }
}
