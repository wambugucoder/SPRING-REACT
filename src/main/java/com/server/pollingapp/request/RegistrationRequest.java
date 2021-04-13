package com.server.pollingapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
public class RegistrationRequest {

    @NotEmpty(message = "UserName Cannot Be Empty")
    @Size(min = 6,message = "Minimum Characters allowed is 6")
    private String username;

    @NotEmpty(message = "Email Cannot Be Empty")
    @Email(message = "Please Input a valid email")
    private String email;

    @NotEmpty(message = "Password Cannot Be Empty")
    @Size(min =6,message ="Password Must Contain atleast 6 characters")
    private String password;

    @NotEmpty(message = "City Cannot Be Empty")
    private String city;

    @NotEmpty(message = "Country Cannot Be Empty")
    private String country;

    public RegistrationRequest(String username, String email, String password, String city, String country) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
