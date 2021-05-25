package com.server.pollingapp.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private static final long serialVersionUID = -5362495346169760055L;
    private Boolean error;
    private String message;
    private String  token;

    public LoginResponse() {
        //EMPTY
    }


    public void setError(Boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
