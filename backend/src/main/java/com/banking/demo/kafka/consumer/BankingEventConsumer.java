package com.banking.demo.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BankingEventConsumer {

    @KafkaListener(
            topics = "corporate-banking-events",
            groupId = "corporate-banking-group"
    )
    public void consume(String message) {
        System.out.println("📩 Kafka Event Received: " + message);
    }
}
