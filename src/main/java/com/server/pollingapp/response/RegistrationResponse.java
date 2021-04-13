package com.server.pollingapp.response;

/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
public class RegistrationResponse {
     public String message;
     public Boolean error;

    public RegistrationResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
