package com.banking.demo.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BankingEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BankingEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String message) {
        kafkaTemplate.send("corporate-banking-events", message);
    }
}
