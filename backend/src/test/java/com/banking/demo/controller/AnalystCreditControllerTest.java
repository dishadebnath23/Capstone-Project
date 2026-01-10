package com.banking.demo.controller;

import com.banking.demo.config.JwtUtil;
import com.banking.demo.dto.CreditRequestReviewRequest;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.repository.UserRepository;
import com.banking.demo.service.CreditRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalystCreditController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnalystCreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditRequestService creditRequestService;

    // 🔑 Required by JwtFilter
    @MockBean
    private JwtUtil jwtUtil;

    // 🔑 ALSO required by JwtFilter
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReviewCreditRequestSuccessfully() throws Exception {

        CreditRequestReviewRequest request = new CreditRequestReviewRequest();
        request.setStatus("APPROVED");
        request.setRemark("Looks good");

        when(creditRequestService.reviewRequest(
                eq("1"), any(), eq("Looks good")
        )).thenReturn(new CreditRequest());

        mockMvc.perform(post("/api/analyst/credit-requests/1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
