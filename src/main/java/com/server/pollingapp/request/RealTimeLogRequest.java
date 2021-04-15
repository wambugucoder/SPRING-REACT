package com.server.pollingapp.request;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RealTimeLogRequest implements Serializable {
    private String level;
    private String message;
    private LocalDateTime createdAt=LocalDateTime.now();
    private  String serviceAffected;

    public RealTimeLogRequest(String level, String message, String serviceAffected) {
        this.level = level;
        this.message = message;
        this.serviceAffected = serviceAffected;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setServiceAffected(String serviceAffected) {
        this.serviceAffected = serviceAffected;
    }
}
