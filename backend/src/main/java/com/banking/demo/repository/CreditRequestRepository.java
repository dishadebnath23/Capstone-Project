package com.banking.demo.repository;

import com.banking.demo.model.CreditRequest;
import com.banking.demo.model.CreditRequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreditRequestRepository
        extends MongoRepository<CreditRequest, String> {

    List<CreditRequest> findByRmUsername(String rmUsername);

    List<CreditRequest> findByStatus(CreditRequestStatus status);

    List<CreditRequest> findAll();
}
