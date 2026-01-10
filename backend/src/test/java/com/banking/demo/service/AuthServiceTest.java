package com.banking.demo.service;

import com.banking.demo.config.JwtUtil;
import com.banking.demo.dto.LoginRequest;
import com.banking.demo.dto.LoginResponse;
import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoginSuccessfully() {

        // Arrange
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

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("rm_user", response.getUsername());
        assertEquals("RELATIONSHIP_MANAGER", response.getRole());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("pass");

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldThrowExceptionWhenUserInactive() {

        LoginRequest request = new LoginRequest();
        request.setUsername("rm_user");
        request.setPassword("pass");

        User user = new User();
        user.setUsername("rm_user");
        user.setActive(false);

        when(userRepository.findByUsername("rm_user"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() {

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

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }
}
