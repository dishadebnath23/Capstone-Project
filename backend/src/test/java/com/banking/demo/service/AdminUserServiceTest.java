package com.banking.demo.service;

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
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    // ---------------- GET ALL USERS ----------------

    @Test
    void shouldReturnAllUsers() {

        User user1 = new User();
        user1.setId("1");

        User user2 = new User();
        user2.setId("2");

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> users = adminUserService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    // ---------------- ACTIVATE USER ----------------

    @Test
    void shouldActivateUserSuccessfully() {

        User user = new User();
        user.setId("123");
        user.setActive(false);

        when(userRepository.findById("123"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        User savedUser = adminUserService.activateUser("123");

        assertTrue(savedUser.isActive());
        verify(userRepository).findById("123");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenActivatingUserNotFound() {

        when(userRepository.findById("999"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminUserService.activateUser("999")
        );

        assertEquals("User not found", ex.getMessage());
    }

    // ---------------- DEACTIVATE USER ----------------

    @Test
    void shouldDeactivateUserSuccessfully() {

        User user = new User();
        user.setId("123");
        user.setActive(true);

        when(userRepository.findById("123"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        User savedUser = adminUserService.deactivateUser("123");

        assertFalse(savedUser.isActive());
        verify(userRepository).findById("123");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingUserNotFound() {

        when(userRepository.findById("999"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminUserService.deactivateUser("999")
        );

        assertEquals("User not found", ex.getMessage());
    }
}
