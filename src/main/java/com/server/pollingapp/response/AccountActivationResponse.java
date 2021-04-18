package com.server.pollingapp.response;

import java.io.Serializable;

public class AccountActivationResponse implements Serializable {
    public Boolean error;
    public String message;

    public AccountActivationResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public AccountActivationResponse() {

    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
