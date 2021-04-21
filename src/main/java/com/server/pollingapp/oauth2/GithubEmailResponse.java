package com.server.pollingapp.oauth2;

public class GithubEmailResponse {
    private String email;
    private boolean primary;
    private boolean verified;
    private String visibility;

    public GithubEmailResponse() {
    }

    public String getEmail() {
        return email;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getVisibility() {
        return visibility;
    }
}
