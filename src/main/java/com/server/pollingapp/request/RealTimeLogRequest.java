package com.server.pollingapp.request;

import java.io.Serializable;
import java.util.Date;

public class RealTimeLogRequest implements Serializable {
    public String level;
    public String message;
    public Date createdAt=new Date();
    public  String serviceAffected;

    public RealTimeLogRequest(String level, String message, String serviceAffected) {
        this.level = level;
        this.message = message;
        this.serviceAffected = serviceAffected;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getServiceAffected() {
        return serviceAffected;
    }

    public void setServiceAffected(String serviceAffected) {
        this.serviceAffected = serviceAffected;
    }
}
