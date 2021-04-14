package com.server.pollingapp.models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "address")
public class AddressModel implements Serializable {

    private static final long serialVersionUID = 8239677412360655401L;
    @Id
    private UUID id=UUID.randomUUID();

    @Column(nullable = false,unique = true)
    private String city;

    @Column(nullable = false,unique = true)
    private String country;

    @OneToOne(mappedBy = "address",orphanRemoval = true)
    private UserModel user;

    public AddressModel(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public AddressModel() {

    }

    public UUID getId() {
        return id;
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
