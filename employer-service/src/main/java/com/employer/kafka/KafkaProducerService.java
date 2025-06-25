package com.employer.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Value("${kafka.topic.job.created}")
    private String jobCreatedTopic;

    @Value("${kafka.topic.job.updated}")
    private String jobUpdatedTopic;

    @Value("${kafka.topic.job.deleted}")
    private String jobDeletedTopic;


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendJobCreatedEvent(String message) {
        kafkaTemplate.send(jobCreatedTopic, message);
    }
    public void sendJobUpdatedEvent(String message) {
        kafkaTemplate.send(jobUpdatedTopic, message);
    }
    public void sendJobDeletedEvent(String message) {
        kafkaTemplate.send(jobDeletedTopic, message);
    }
}

