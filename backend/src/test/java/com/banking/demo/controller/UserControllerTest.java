package com.banking.demo.controller;

import com.banking.demo.model.User;
import com.banking.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    // 🔑 THIS IS THE MISSING PIECE
    @MockBean
    private com.banking.demo.config.JwtFilter jwtFilter;

    @Test
    @WithMockUser(username = "user")
    void shouldReturnCurrentUser() throws Exception {

        User user = new User();
        user.setUsername("user");

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());
    }
}

