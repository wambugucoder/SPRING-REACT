package com.server.pollingapp.service;

import com.google.gson.Gson;
import com.server.pollingapp.exception.OAuth2AuthenticationProcessingException;
import com.server.pollingapp.models.AuthProvider;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.oauth2.GithubEmailResponse;
import com.server.pollingapp.oauth2.OAuth2UserInfo;
import com.server.pollingapp.oauth2.OAuth2UserInfoFactory;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.security.PollsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * An implementation of an {@link OAuth2UserService} that supports standard OAuth 2.0
 * Provider's.
 * <p>
 * For standard OAuth 2.0 Provider's, the attribute name used to access the user's name
 * from the UserInfo response is required and therefore must be available via
 * {@link ClientRegistration.ProviderDetails.UserInfoEndpoint#getUserNameAttributeName()
 * UserInfoEndpoint.getUserNameAttributeName()}.
 * <p>
 * <b>NOTE:</b> Attribute names are <b>not</b> standardized between providers and
 * therefore will vary. Please consult the provider's API documentation for the set of
 * supported user attribute names.
 *
 *
 * @since 5.0
 * @see OAuth2UserService
 * @see OAuth2UserRequest
 * @see OAuth2User
 * @see DefaultOAuth2User
 */
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    @Autowired UserRepositoryImpl userRepositoryImpl;

    @Autowired UserRepository userRepository;




    /**
     *
     * @param oAuth2UserRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Once an access token has been granted,this method will get to capture attributes of the user,
     * map them to specific oauth2.0 providers userinfo then save users if their emails arent on database.
     * Also,if a user switches providers,the method should be able to update the username,image_url and provider
     * and all content will be safe since email and id will not be interfered with.
     * @param oAuth2UserRequest
     * @param oAuth2User
     * @return
     */

    private  OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //LINK THE USERFINFO WITH THE PROVIDER TYPE FIRSTLY
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        //IF NO EMAIL IS FOUND THROW AN ERROR AS AN EMAIL IS THE KEY-POINT OF ALL OF THIS
        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            //AN EMAIL WILL NOT BE GENERATED IN THE FIRST INSTANCE IF AUTHENICATING WITH GITHUB.
            //SO IF EMPTY AND PROVIDER IS GITHUB->GENERATE A TOKEN AND FIND THE EMAIL THE LONG WAY->SEE REQUEST EMAIL METHOD
            if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase("github")) {
                oAuth2UserInfo.setEmail(requestEmail(oAuth2UserRequest.getAccessToken().getTokenValue()));
            } else {
                throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
            }
        }

        Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByEmail(oAuth2UserInfo.getEmail()));
        UserModel user;
        //IF USER IS FOUND AND HAD LOGGED IN VIA LOCAL AUTH,THROW AN ERROR
        //REASON FOR THROWING AN ERROR->UPDATING USER FROM LOCAL TO OTHER PROVIDER MAY BRING ABOUT PASSWORD LOSS
        //AND USER MIGHT GO BACK TO LOCAL AND FIND MISSING CREDENTILAS IF AT ALL USER WAS TO REVERT BACK TO LOCAL
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getAuthProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                if (user.getAuthProvider().equals(AuthProvider.local)){
                    throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                            user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                            " account to login.");
                }
                //IF USER HAS A DIFFERENT AUTH PROVIDER
                //EMAIL REMAINS CONSTANT
                //UPDATE DETAILS->USERNAME,AVATAR AND AUTH PROVIDER.

                updateExistingUser(user, oAuth2UserInfo,oAuth2UserRequest);
            }
            //IF USER HAS SAME AUTH PROVIDER,PERFORM AN UPDATE
            //USER MAY HAVE UPDATED THEIR AVATAR OR USERNAME THROUGH THE OAUTH2.0 PROVIDER
            user = updateExistingUser(user, oAuth2UserInfo,oAuth2UserRequest);
        }
        //IF NO USER WAS FOUND ,CURRENT USER IS NEW AND HAS TO BE REGISTERED IN SYSTEM
        else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return  PollsUserDetails.create(user,oAuth2User.getAttributes());
    }

    private UserModel registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        UserModel user = new UserModel();

        user.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setUsername(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageurl(oAuth2UserInfo.getImageUrl());
        user.setEnabled(true);
        user.setAccountNotLocked(true);
        return userRepositoryImpl.addUser(user);
    }

    private UserModel updateExistingUser(UserModel existingUser, OAuth2UserInfo oAuth2UserInfo,OAuth2UserRequest oAuth2UserRequest) {
        existingUser.setUsername(oAuth2UserInfo.getName());
        existingUser.setImageurl(oAuth2UserInfo.getImageUrl());
        existingUser.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        return userRepositoryImpl.updateUser(existingUser);
    }
    //SINCE EMAIL CANNOT BE RETRIEVED DIRECTLY FROM GITHUB
    //THIS METHODS FETCHES PRIMARY EMAIL BY USE OF REST TEMPLATES AND ACCESS TOKEN GENERATED
    private String requestEmail(String token) {
        String url = "https://api.github.com/user/emails";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class, 1);

        if (response.getStatusCode() == HttpStatus.OK) {
            Gson g = new Gson();
            GithubEmailResponse[] emails = g.fromJson(response.getBody(), GithubEmailResponse[].class);

            String primaryEmail = "";
            for(GithubEmailResponse email: emails)
                if (email.isPrimary()) {
                    primaryEmail = email.getEmail();
                    break;
                }
            return primaryEmail;
        } else {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
    }


}
