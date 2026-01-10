package com.banking.demo.controller;

import com.banking.demo.config.JwtUtil;
import com.banking.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestExceptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔑 REQUIRED mocks to stop JwtFilter crash
    @MockBean
    private com.banking.demo.config.JwtUtil jwtUtil;

    @MockBean
    private com.banking.demo.repository.UserRepository userRepository;

    @Test
    void shouldHandleBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad request"));
    }

    @Test
    void shouldHandleResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Something went wrong"));
    }
}







