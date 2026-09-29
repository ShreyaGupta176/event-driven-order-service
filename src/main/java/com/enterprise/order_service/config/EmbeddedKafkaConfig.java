package com.enterprise.order_service.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

@Configuration
public class EmbeddedKafkaConfig {

    private EmbeddedKafkaZKBroker embeddedKafkaBroker;

    @PostConstruct
    public void startEmbeddedKafka() {
        // Concrete implementation for Spring Kafka 3.x
        embeddedKafkaBroker = new EmbeddedKafkaZKBroker(1, true, 3, "order-events");
        embeddedKafkaBroker.kafkaPorts(9092);
        embeddedKafkaBroker.afterPropertiesSet();
    }

    @PreDestroy
    public void stopEmbeddedKafka() {
        if (embeddedKafkaBroker != null) {
            embeddedKafkaBroker.destroy();
        }
    }
}