package com.server.pollingapp.oauth2;


import java.util.HashMap;
import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public void setEmail(String email) {
        attributes = new HashMap<>(attributes);
        attributes.put("email", email);
    }

}

