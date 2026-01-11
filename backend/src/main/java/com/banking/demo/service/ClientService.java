package com.banking.demo.service;

import com.banking.demo.dto.ClientCreateRequest;
import com.banking.demo.kafka.KafkaEventProducer;
import com.banking.demo.model.Client;
import com.banking.demo.model.PrimaryContact;
import com.banking.demo.repository.ClientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.banking.demo.kafka.producer.BankingEventProducer;

import java.util.List;
import com.banking.demo.exception.ResourceNotFoundException;

@Service
public class ClientService {

    private final ClientRepository clientRepo;
    private final KafkaEventProducer kafkaEventProducer;

    public ClientService(
            ClientRepository clientRepo,
            KafkaEventProducer kafkaEventProducer
    ) {
        this.clientRepo = clientRepo;
        this.kafkaEventProducer = kafkaEventProducer;
    }


    // ---------------- CREATE CLIENT (RM ONLY) ----------------
    public Client createClient(ClientCreateRequest request) {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Client client = new Client();
        client.setCompanyName(request.getCompanyName());
        client.setIndustry(request.getIndustry());
        client.setAddress(request.getAddress());
        client.setAnnualTurnover(request.getAnnualTurnover());
        client.setDocumentsSubmitted(request.isDocumentsSubmitted());

        PrimaryContact contact = new PrimaryContact();
        contact.setName(request.getPrimaryContactName());
        contact.setEmail(request.getPrimaryContactEmail());
        contact.setPhone(request.getPrimaryContactPhone());

        client.setPrimaryContact(contact);
        client.setRmUsername(rmUsername);

        Client savedClient = clientRepo.save(client);

// 🔔 Kafka event
        kafkaEventProducer.send(
                "CLIENT_CREATED : " + savedClient.getId()
        );

        return savedClient;

    }
        // ---------------- GET OWN CLIENTS (RM ONLY) ----------------
    public List<Client> getMyClients() {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return clientRepo.findByRmUsername(rmUsername);
    }

    // ---------------- GET SINGLE CLIENT (RM ONLY, OWN DATA) ----------------
    public Client getClientById(String id) {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return clientRepo.findByIdAndRmUsername(id, rmUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    // ---------------- UPDATE CLIENT (RM ONLY, OWN DATA) ----------------
    public Client updateClient(String id, ClientCreateRequest request) {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Client client = clientRepo.findByIdAndRmUsername(id, rmUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        // Update allowed fields
        client.setCompanyName(request.getCompanyName());
        client.setIndustry(request.getIndustry());
        client.setAddress(request.getAddress());
        client.setAnnualTurnover(request.getAnnualTurnover());
        client.setDocumentsSubmitted(request.isDocumentsSubmitted());

        // Update primary contact
        PrimaryContact contact = client.getPrimaryContact();
        if (contact == null) {
            contact = new PrimaryContact();
        }

        contact.setName(request.getPrimaryContactName());
        contact.setEmail(request.getPrimaryContactEmail());
        contact.setPhone(request.getPrimaryContactPhone());

        client.setPrimaryContact(contact);

        // rmUsername is NOT changed (security guarantee)
        return clientRepo.save(client);
    }

    // ---------------- SEARCH / FILTER CLIENTS (RM ONLY) ----------------
    public List<Client> searchClients(String companyName, String industry) {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        boolean hasCompany = companyName != null && !companyName.isBlank();
        boolean hasIndustry = industry != null && !industry.isBlank();

        if (hasCompany && hasIndustry) {
            return clientRepo
                    .findByRmUsernameAndCompanyNameContainingIgnoreCaseAndIndustryContainingIgnoreCase(
                            rmUsername,
                            companyName.trim(),
                            industry.trim()
                    );
        }


        if (hasCompany) {
            return clientRepo
                    .findByRmUsernameAndCompanyNameContainingIgnoreCase(
                            rmUsername, companyName
                    );
        }

        if (hasIndustry) {
            return clientRepo
                    .findByRmUsernameAndIndustryIgnoreCase(
                            rmUsername, industry
                    );
        }

        return clientRepo.findByRmUsername(rmUsername);
    }
}
