package com.server.pollingapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserModel implements Serializable {
    private static final long serialVersionUID = -999055560350713378L;

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private String imageurl="none";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRoles roles = UserRoles.ROLE_USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AuthProvider authProvider;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isEnabled = false;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isAccountNotLocked = false;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isAccountNonExpired = true;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isEmailVerificationSent=false;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PollModel> polls = Collections.emptyList();

    @CreatedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;



    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;
    


    public UserModel() {

    }

    public UserModel(String username, String email, String password, AuthProvider authProvider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Boolean getEmailVerificationSent() {
        return isEmailVerificationSent;
    }

    public void setEmailVerificationSent(Boolean emailVerificationSent) {
        isEmailVerificationSent = emailVerificationSent;
    }

    public List<PollModel> getPolls() {
        return polls;
    }

    public void setPolls(List<PollModel> createdBy) {
        this.polls = createdBy;
    }
}