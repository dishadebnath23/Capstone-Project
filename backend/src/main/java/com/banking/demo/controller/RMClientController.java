package com.banking.demo.controller;

import com.banking.demo.dto.ClientCreateRequest;
import com.banking.demo.model.Client;
import com.banking.demo.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rm/clients")
@PreAuthorize("hasRole('RM')")
@Validated
public class RMClientController {

    private final ClientService clientService;

    public RMClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ---------------- CREATE CLIENT ----------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client createClient(
            @Valid @RequestBody ClientCreateRequest request
    ) {
        return clientService.createClient(request);
    }

    // ---------------- GET ALL OWN CLIENTS ----------------
    @GetMapping
    public List<Client> getMyClients() {
        return clientService.getMyClients();
    }

    // ---------------- GET SINGLE CLIENT (OWN DATA) ----------------
    @GetMapping("/{id}")
    public Client getClientById(@PathVariable String id) {
        return clientService.getClientById(id);
    }

    // ---------------- SEARCH CLIENTS ----------------
    @GetMapping("/search")
    public List<Client> searchClients(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String industry
    ) {
        return clientService.searchClients(companyName, industry);
    }

    // ---------------- UPDATE CLIENT ----------------
    @PutMapping("/{id}")
    public Client updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientCreateRequest request
    ) {
        return clientService.updateClient(id, request);
    }
}
