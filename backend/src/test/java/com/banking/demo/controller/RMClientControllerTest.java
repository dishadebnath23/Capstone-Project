package com.banking.demo.controller;
import com.banking.demo.config.JwtFilter;
import com.banking.demo.controller.RMClientController;
import com.banking.demo.dto.ClientCreateRequest;
import com.banking.demo.model.Client;
import com.banking.demo.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RMClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class RMClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private JwtFilter jwtFilter; // 🔑 VERY IMPORTANT

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- CREATE CLIENT ----------------
    @Test
    @WithMockUser(roles = "RM")
    void shouldCreateClient() throws Exception {

        ClientCreateRequest request = new ClientCreateRequest();
        request.setCompanyName("ABC Pvt Ltd");
        request.setIndustry("IT");
        request.setAddress("Bangalore");
        request.setAnnualTurnover(10_000_000);

        request.setPrimaryContactName("John Doe");
        request.setPrimaryContactEmail("john@abc.com");
        request.setPrimaryContactPhone("9876543210");

        Client client = new Client();
        client.setId("1");
        client.setCompanyName("ABC Pvt Ltd");

        when(clientService.createClient(any(ClientCreateRequest.class)))
                .thenReturn(client);

        mockMvc.perform(post("/api/rm/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    // ---------------- GET ALL CLIENTS ----------------
    @Test
    @WithMockUser(roles = "RM")
    void shouldReturnMyClients() throws Exception {

        when(clientService.getMyClients())
                .thenReturn(List.of(new Client()));

        mockMvc.perform(get("/api/rm/clients"))
                .andExpect(status().isOk());
    }

    // ---------------- GET CLIENT BY ID ----------------
    @Test
    @WithMockUser(roles = "RM")
    void shouldReturnClientById() throws Exception {

        when(clientService.getClientById("1"))
                .thenReturn(new Client());

        mockMvc.perform(get("/api/rm/clients/1"))
                .andExpect(status().isOk());
    }

    // ---------------- SEARCH CLIENTS ----------------
    @Test
    @WithMockUser(roles = "RM")
    void shouldSearchClients() throws Exception {

        when(clientService.searchClients("ABC", "IT"))
                .thenReturn(List.of(new Client()));

        mockMvc.perform(get("/api/rm/clients/search")
                        .param("companyName", "ABC")
                        .param("industry", "IT"))
                .andExpect(status().isOk());
    }

    // ---------------- UPDATE CLIENT ----------------
    @Test
    @WithMockUser(roles = "RM")
    void shouldUpdateClient() throws Exception {

        ClientCreateRequest request = new ClientCreateRequest();
        request.setCompanyName("ABC Pvt Ltd");
        request.setIndustry("IT");
        request.setAddress("Bangalore");
        request.setAnnualTurnover(15_000_000);

        request.setPrimaryContactName("John Doe");
        request.setPrimaryContactEmail("john@abc.com");
        request.setPrimaryContactPhone("9876543210");

        Client updatedClient = new Client();
        updatedClient.setId("1");
        updatedClient.setCompanyName("ABC Pvt Ltd");

        when(clientService.updateClient(eq("1"), any(ClientCreateRequest.class)))
                .thenReturn(updatedClient);

        mockMvc.perform(put("/api/rm/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}
