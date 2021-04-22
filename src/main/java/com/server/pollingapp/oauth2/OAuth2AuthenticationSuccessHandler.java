package com.server.pollingapp.oauth2;

import com.server.pollingapp.exception.BadRequestException;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.security.PollsUserDetails;
import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.PollStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.server.pollingapp.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  final
  HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  final
  JwtService jwtService;

  final
  PollStream pollStream;

  final
  UserRepository userRepository;

  @Value("${app.oauth2.authorizedRedirectUris}")
  String redirectUrRI;

    public OAuth2AuthenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, JwtService jwtService, PollStream pollStream, UserRepository userRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.jwtService = jwtService;
        this.pollStream = pollStream;
        this.userRepository = userRepository;
    }


    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @since 5.2.0
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR","Response has already been committed. Unable to redirect to " + targetUrl,"OAuth-Success-Handler"));
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = jwtService.GenerateOauthToken(authentication);
        

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token",token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        String authorizedRedirectUri=redirectUrRI;
        // Only validate host and port. Let the clients use different paths if they want to
        URI authorizedURI = URI.create(authorizedRedirectUri);
        return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort();
    }
}
