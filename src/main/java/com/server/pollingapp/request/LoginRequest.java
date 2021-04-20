package com.server.pollingapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotEmpty(message = "UserName Cannot Be Empty")
    @Email(message = "Please Input a valid email")
    private String email;


    @NotEmpty(message = "Password Cannot Be Empty")
    @Size(min =6,message ="Password Must Contain atleast 6 characters")
    private String password;

    public LoginRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
