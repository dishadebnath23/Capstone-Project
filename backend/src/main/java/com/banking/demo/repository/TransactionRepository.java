package com.banking.demo.repository;

import com.banking.demo.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository
        extends MongoRepository<Transaction, String> {

    List<Transaction> findByStatus(String status);
}


