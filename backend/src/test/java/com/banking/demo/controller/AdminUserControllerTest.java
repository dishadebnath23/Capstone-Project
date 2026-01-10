package com.banking.demo.controller;

import com.banking.demo.dto.UserResponseDto;
import com.banking.demo.service.UserService;
import com.banking.demo.config.JwtFilter;
import com.banking.demo.config.JwtUtil;
import com.banking.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔑 disable security filters
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // 🔑 mock security-related beans
    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN") // 🔑 simulate ADMIN user
    void shouldReturnAllUsers() throws Exception {

        UserResponseDto u1 =
                new UserResponseDto("1", "admin", "ADMIN", true);

        UserResponseDto u2 =
                new UserResponseDto("2", "rm_user", "RM", true);

        when(userService.getAllUsers())
                .thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("rm_user"));
    }
}
