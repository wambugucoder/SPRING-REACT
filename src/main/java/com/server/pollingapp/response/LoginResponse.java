package com.server.pollingapp.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private static final long serialVersionUID = -5362495346169760055L;
      public Boolean error;
      public String message;
      public String  token;

    public LoginResponse() {
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
