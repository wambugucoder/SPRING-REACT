package com.server.pollingapp.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserModel implements Serializable {
    private static final long serialVersionUID = -999055560350713378L;

           @Id
           private  UUID id=UUID.randomUUID();

           @Column(nullable = false,unique = true)
           private String username;

           @Column(nullable = false,unique = true)
           private String email;

           @Column(nullable = false)
           private String password;

           @Column(nullable = false)
           @Enumerated(EnumType.STRING)
           private  UserRoles roles = UserRoles.ROLE_USER;

           @Column(nullable = false)
           private Boolean isEnabled=false;

           @Column(nullable = false)
           private Boolean isAccountNotLocked=false;

           @Column(nullable = false)
           private Boolean isAccountNonExpired=true;

           @CreatedDate
           @Column(nullable = false, columnDefinition = "TIMESTAMP")
           private LocalDateTime createdAt;

           @LastModifiedDate
           @Column(nullable = false, columnDefinition = "TIMESTAMP")
           private LocalDateTime updateAt;

           @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
           @JoinColumn(name = "address_id",referencedColumnName = "id")
           private AddressModel address;


    public UserModel(String username, String email, String password, AddressModel address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }


    public UserModel() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UserRoles getRoles() {
        return roles;
    }

    public void setRoles(UserRoles roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getAccountNotLocked() {
        return isAccountNotLocked;
    }

    public void setAccountNotLocked(Boolean accountNotLocked) {
        isAccountNotLocked = accountNotLocked;
    }

    public Boolean getAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }
}
