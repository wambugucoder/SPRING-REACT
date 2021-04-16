package com.server.pollingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.pollingapp.request.RealTimeLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PollStream {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${message.topic}")
    private String topic;

    public void sendToMessageBroker(RealTimeLogRequest realTimeLogRequest)  {

        kafkaTemplate.send(topic, realTimeLogRequest);

    }
}
