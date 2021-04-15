package com.server.pollingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.pollingapp.request.RealTimeLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class PollStream {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Value("${message.topic}")
    private String topic;

    public ListenableFuture<SendResult<String, String>> sendToMessageBroker(RealTimeLogRequest realTimeLogRequest)  {
        String sendToBroker = null;
        try {
            sendToBroker = objectMapper.writeValueAsString(realTimeLogRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kafkaTemplate.send(topic,sendToBroker);

    }
}
