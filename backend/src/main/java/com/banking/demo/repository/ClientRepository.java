package com.banking.demo.repository;

import com.banking.demo.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {

    // -------- BASIC --------
    List<Client> findByRmUsername(String rmUsername);

    Optional<Client> findByIdAndRmUsername(String id, String rmUsername);

    // -------- SEARCH / FILTER --------
    List<Client> findByRmUsernameAndCompanyNameContainingIgnoreCase(
            String rmUsername,
            String companyName
    );

    List<Client> findByRmUsernameAndIndustryIgnoreCase(
            String rmUsername,
            String industry
    );

    List<Client> findByRmUsernameAndCompanyNameContainingIgnoreCaseAndIndustryContainingIgnoreCase(
            String rmUsername,
            String companyName,
            String industry
    );
}
