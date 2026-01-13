package com.banking.demo.service;

import com.banking.demo.dto.ClientCreateRequest;
import com.banking.demo.exception.ResourceNotFoundException;
import com.banking.demo.kafka.KafkaEventProducer;
import com.banking.demo.model.Client;
import com.banking.demo.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private KafkaEventProducer kafkaEventProducer;

    @InjectMocks
    private ClientService clientService;

    private void mockLoggedInUser(String username) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
    }

    // ================= TEST 1 =================
    @Test
    void shouldCreateClientAndSetRmUsername() {
        doNothing().when(kafkaEventProducer).send(anyString());
        mockLoggedInUser("rm_user");

        ClientCreateRequest request = new ClientCreateRequest();
        request.setCompanyName("ABC");
        request.setIndustry("Manufacturing");
        request.setAddress("Mumbai");
        request.setAnnualTurnover(10.0);
        request.setPrimaryContactName("A");
        request.setPrimaryContactEmail("a@a.com");
        request.setPrimaryContactPhone("1234567890");

        when(clientRepository.save(any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Client saved = clientService.createClient(request);

        assertEquals("rm_user", saved.getRmUsername());
        verify(clientRepository).save(any(Client.class));
    }

    // ================= TEST 2 =================
    @Test
    void shouldReturnOnlyClientsOfLoggedInRm() {

        mockLoggedInUser("rm_user");

        Client c1 = new Client();
        c1.setCompanyName("ABC");

        Client c2 = new Client();
        c2.setCompanyName("XYZ");

        when(clientRepository.findByRmUsername("rm_user"))
                .thenReturn(List.of(c1, c2));

        List<Client> clients = clientService.getMyClients();

        assertEquals(2, clients.size());
        assertEquals("ABC", clients.get(0).getCompanyName());
        assertEquals("XYZ", clients.get(1).getCompanyName());

        verify(clientRepository).findByRmUsername("rm_user");
    }

    // ================= TEST 3 =================
    @Test
    void shouldThrowExceptionWhenClientNotFound() {

        mockLoggedInUser("rm_user");

        when(clientRepository.findByIdAndRmUsername("x", "rm_user"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.getClientById("x")
        );

        verify(clientRepository)
                .findByIdAndRmUsername("x", "rm_user");
    }

    // ================= TEST 4 =================
    @Test
    void shouldReturnClientWhenFound() {

        String rmUsername = "rm_user";
        mockLoggedInUser(rmUsername);

        Client client = new Client();
        client.setId("c1");
        client.setCompanyName("ABC");
        client.setRmUsername(rmUsername);

        when(clientRepository.findByIdAndRmUsername("c1", rmUsername))
                .thenReturn(Optional.of(client));

        Client result = clientService.getClientById("c1");

        assertEquals("ABC", result.getCompanyName());
    }

    // ================= TEST 5 =================
    @Test
    void shouldThrowExceptionWhenNoAuthenticationPresent() {

        SecurityContextHolder.clearContext();

        assertThrows(
                NullPointerException.class,
                () -> clientService.getMyClients()
        );
    }
    // ================= TEST 6 =================
    @Test
    void shouldUpdateClientAndCreatePrimaryContactIfMissing() {

        mockLoggedInUser("rm_user");

        Client existingClient = new Client();
        existingClient.setId("c1");
        existingClient.setRmUsername("rm_user");
        existingClient.setPrimaryContact(null); // 🔑 forces branch

        when(clientRepository.findByIdAndRmUsername("c1", "rm_user"))
                .thenReturn(Optional.of(existingClient));

        when(clientRepository.save(any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClientCreateRequest request = new ClientCreateRequest();
        request.setCompanyName("Updated Co");
        request.setIndustry("IT");
        request.setAddress("Pune");
        request.setAnnualTurnover(50.0);
        request.setDocumentsSubmitted(true);
        request.setPrimaryContactName("John");
        request.setPrimaryContactEmail("john@test.com");
        request.setPrimaryContactPhone("9999999999");

        Client updated = clientService.updateClient("c1", request);

        assertNotNull(updated.getPrimaryContact());
        assertEquals("Updated Co", updated.getCompanyName());
    }
    // ================= TEST 7 =================
    @Test
    void shouldSearchClientsByCompanyAndIndustry() {

        mockLoggedInUser("rm_user");

        when(clientRepository
                .findByRmUsernameAndCompanyNameContainingIgnoreCaseAndIndustryContainingIgnoreCase(
                        "rm_user", "ABC", "IT"))
                .thenReturn(List.of(new Client()));

        List<Client> result = clientService.searchClients("ABC", "IT");

        assertEquals(1, result.size());
    }

    // ================= TEST 8 =================
    @Test
    void shouldSearchClientsByCompanyOnly() {

        mockLoggedInUser("rm_user");

        when(clientRepository
                .findByRmUsernameAndCompanyNameContainingIgnoreCase("rm_user", "ABC"))
                .thenReturn(List.of(new Client()));

        List<Client> result = clientService.searchClients("ABC", null);

        assertEquals(1, result.size());
    }

    // ================= TEST 9 =================
    @Test
    void shouldSearchClientsByIndustryOnly() {

        mockLoggedInUser("rm_user");

        when(clientRepository
                .findByRmUsernameAndIndustryIgnoreCase("rm_user", "IT"))
                .thenReturn(List.of(new Client()));

        List<Client> result = clientService.searchClients(null, "IT");

        assertEquals(1, result.size());
    }
    // ================= TEST 10 =================
    @Test
    void shouldReturnAllClientsWhenSearchParamsAreBlank() {

        mockLoggedInUser("rm_user");

        when(clientRepository.findByRmUsername("rm_user"))
                .thenReturn(List.of(new Client(), new Client()));

        List<Client> result = clientService.searchClients("   ", "   ");

        assertEquals(2, result.size());
    }



}



