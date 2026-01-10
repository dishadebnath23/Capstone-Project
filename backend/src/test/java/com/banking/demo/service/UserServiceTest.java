package com.banking.demo.service;

import com.banking.demo.dto.UpdateUserRequest;
import com.banking.demo.dto.UserResponseDto;
import com.banking.demo.exception.ResourceNotFoundException;
import com.banking.demo.exception.UnauthorizedActionException;
import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // ---------------- GET ALL USERS ----------------
    @Test
    void shouldReturnAllUsersAsResponseDto() {

        User user1 = new User();
        user1.setId("1");
        user1.setUsername("user1");
        user1.setRole(User.Role.RELATIONSHIP_MANAGER);
        user1.setActive(true);

        User user2 = new User();
        user2.setId("2");
        user2.setUsername("user2");
        user2.setRole(User.Role.ANALYST);
        user2.setActive(false);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("RELATIONSHIP_MANAGER", result.get(0).getRole());
        assertTrue(result.get(0).isActive());
    }

    // ---------------- UPDATE USER STATUS (SUCCESS) ----------------
    @Test
    void shouldUpdateUserStatusSuccessfully() {

        User user = new User();
        user.setId("123");
        user.setUsername("rm_user");
        user.setRole(User.Role.RELATIONSHIP_MANAGER);
        user.setActive(false);

        when(userRepository.findById("123"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        UserResponseDto response =
                userService.updateUserStatus("123", true);

        assertTrue(response.isActive());
        assertEquals("rm_user", response.getUsername());
    }

    // ---------------- UPDATE USER STATUS (ADMIN FORBIDDEN) ----------------
    @Test
    void shouldThrowExceptionWhenUpdatingAdminStatus() {

        User admin = new User();
        admin.setId("admin1");
        admin.setRole(User.Role.ADMIN);

        when(userRepository.findById("admin1"))
                .thenReturn(Optional.of(admin));

        UnauthorizedActionException ex = assertThrows(
                UnauthorizedActionException.class,
                () -> userService.updateUserStatus("admin1", false)
        );

        assertEquals(
                "Admin users cannot be deactivated",
                ex.getMessage()
        );
    }

    // ---------------- UPDATE USER STATUS (USER NOT FOUND) ----------------
    @Test
    void shouldThrowExceptionWhenUpdatingStatusUserNotFound() {

        when(userRepository.findById("999"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.updateUserStatus("999", false)
        );

        assertEquals("User not found", ex.getMessage());
    }

    // ---------------- UPDATE USER DETAILS (SUCCESS) ----------------
    @Test
    void shouldUpdateUserDetailsSuccessfully() {

        User user = new User();
        user.setId("123");
        user.setUsername("rm_user");
        user.setRole(User.Role.RELATIONSHIP_MANAGER);
        user.setEmail("old@email.com");
        user.setActive(true);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("new@email.com");
        request.setRole("ANALYST");

        when(userRepository.findById("123"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        UserResponseDto response =
                userService.updateUser("123", request);

        assertEquals("ANALYST", response.getRole());
        assertEquals("rm_user", response.getUsername());
    }

    // ---------------- UPDATE USER DETAILS (ADMIN FORBIDDEN) ----------------
    @Test
    void shouldThrowExceptionWhenUpdatingAdminUser() {

        User admin = new User();
        admin.setId("admin1");
        admin.setRole(User.Role.ADMIN);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("new@email.com");

        when(userRepository.findById("admin1"))
                .thenReturn(Optional.of(admin));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.updateUser("admin1", request)
        );

        assertEquals("Admin user cannot be modified", ex.getMessage());
    }

    // ---------------- UPDATE USER DETAILS (USER NOT FOUND) ----------------
    @Test
    void shouldThrowExceptionWhenUpdatingUserNotFound() {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("new@email.com");

        when(userRepository.findById("999"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser("999", request)
        );

        assertEquals("User not found", ex.getMessage());
    }
}
