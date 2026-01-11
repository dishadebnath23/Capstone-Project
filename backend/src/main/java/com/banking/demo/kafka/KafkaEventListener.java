package com.banking.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaEventListener.class);

    @KafkaListener(
            topics = "corporate-banking-events",
            groupId = "corporate-banking-group"
    )
    public void consume(String message) {
        log.info("📩 Kafka Event Received: {}", message);
    }
}
