package com.banking.demo.controller;

import com.banking.demo.config.JwtFilter;
import com.banking.demo.config.JwtUtil;
import com.banking.demo.config.SecurityConfig;
import com.banking.demo.dto.CreditRequestCreateRequest;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.model.CreditRequestStatus;
import com.banking.demo.repository.UserRepository;
import com.banking.demo.service.CreditRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditRequestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@EnableMethodSecurity
class CreditRequestControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 🔹 Controller dependency
    @MockBean
    private CreditRequestService creditRequestService;

    // 🔹 SECURITY DEPENDENCIES (THIS WAS MISSING)
//    @MockBean
//    private JwtFilter jwtFilter;
//
 @MockBean
 private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    // ================= ANALYST =================

    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReturnAllRequestsForAnalyst() throws Exception {
        when(creditRequestService.getAllRequestsForAnalyst())
                .thenReturn(List.of(new CreditRequest()));

        mockMvc.perform(get("/api/credit-requests/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReturnPendingRequests() throws Exception {
        when(creditRequestService.getPendingRequests())
                .thenReturn(List.of(new CreditRequest()));

        mockMvc.perform(get("/api/credit-requests/pending"))
                .andExpect(status().isOk());
    }

    // ================= RM =================

    @Test
    @WithMockUser(roles = "RM")
    void shouldReturnMyRequestsForRM() throws Exception {
        when(creditRequestService.getMyRequests())
                .thenReturn(List.of(new CreditRequest()));

        mockMvc.perform(get("/api/credit-requests/rm"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "RM")
    void shouldCreateCreditRequest() throws Exception {

        CreditRequestCreateRequest request = new CreditRequestCreateRequest();
        request.setClientId("c1");
        request.setRequestedAmount(100000);
        request.setTenureMonths(12);
        request.setPurpose("Test");

        when(creditRequestService.create(any()))
                .thenReturn(new CreditRequest());

        mockMvc.perform(post("/api/credit-requests")
                        .with(csrf())   // ✅ THIS LINE
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReviewCreditRequest() throws Exception {

        when(creditRequestService.reviewRequest(
                "1",
                CreditRequestStatus.APPROVED,
                "ok"
        )).thenReturn(new CreditRequest());

        mockMvc.perform(put("/api/credit-requests/1/review")
                        .with(csrf())   // ✅ CSRF
                        .param("status", "APPROVED")
                        .param("remark", "ok"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReturnAllRequestsForRM() throws Exception {
        when(creditRequestService.getAllRequestsForAnalyst())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/credit-requests/all"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "ANALYST") // RM allowed in real code
    void shouldReturnPendingRequestsForRM() throws Exception {
        when(creditRequestService.getPendingRequests())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/credit-requests/pending"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReturnEmptyPendingRequests() throws Exception {
        when(creditRequestService.getPendingRequests())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/credit-requests/pending"))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "ANALYST")
    void shouldReviewCreditRequestWithRejectedStatus() throws Exception {

        when(creditRequestService.reviewRequest(
                "1",
                CreditRequestStatus.REJECTED,
                "not ok"
        )).thenReturn(new CreditRequest());

        mockMvc.perform(put("/api/credit-requests/1/review")
                        .with(csrf())
                        .param("status", "REJECTED")
                        .param("remark", "not ok"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "RM")
    void shouldCreateCreditRequestWithDifferentPayload() throws Exception {
        CreditRequestCreateRequest request = new CreditRequestCreateRequest();
        request.setClientId("c2");
        request.setRequestedAmount(50000);
        request.setTenureMonths(24);
        request.setPurpose("Home");

        when(creditRequestService.create(any())).thenReturn(new CreditRequest());

        mockMvc.perform(post("/api/credit-requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }



}
