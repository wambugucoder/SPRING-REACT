package com.server.pollingapp.oauth2;


import com.server.pollingapp.exception.OAuth2AuthenticationProcessingException;
import com.server.pollingapp.models.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {
    /**
     * The Method below is used to get the registration id of the oauth2.0 provider
     * and map it to its correct oauthUserInfo .
     * @param registrationId
     * @param attributes
     * @return
     */

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);

        }

        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}

