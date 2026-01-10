package com.banking.demo.service;

import com.banking.demo.dto.UserResponseDto;
import com.banking.demo.exception.UnauthorizedActionException;
import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.banking.demo.dto.UpdateUserRequest;
import com.banking.demo.exception.ResourceNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name(),
                        user.isActive()
                ))
                .toList();
    }

    public UserResponseDto updateUserStatus(String id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.ADMIN) {
            throw new UnauthorizedActionException("Admin users cannot be deactivated");
        }


        user.setActive(active);
        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name(),
                saved.isActive()
        );
    }
    public UserResponseDto updateUser(String id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Prevent editing ADMIN role user
        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Admin user cannot be modified");
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(User.Role.valueOf(request.getRole()));
        }

        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name(),
                saved.isActive()
        );
    }




}




