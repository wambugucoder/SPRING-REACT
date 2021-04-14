package com.server.pollingapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotEmpty(message = "UserName Cannot Be Empty")
    @Size(min = 6,message = "Minimum Characters allowed is 6")
    private String username;


    @NotEmpty(message = "Password Cannot Be Empty")
    @Size(min =6,message ="Password Must Contain atleast 6 characters")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
