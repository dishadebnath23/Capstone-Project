package com.banking.demo.controller;

import com.banking.demo.dto.UserResponseDto;
import com.banking.demo.model.User;
import com.banking.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.banking.demo.dto.UpdateUserRequest;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users


    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }


    // UPDATE user active status
    @PutMapping("/{id}/status")
    public UserResponseDto updateUserStatus(
            @PathVariable String id,
            @RequestParam boolean active
    ) {
        return userService.updateUserStatus(id, active);
    }

  


}
