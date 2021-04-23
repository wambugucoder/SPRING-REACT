package com.server.pollingapp.security;


import com.server.pollingapp.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.server.pollingapp.oauth2.OAuth2AuthenticationFailureHandler;
import com.server.pollingapp.oauth2.OAuth2AuthenticationSuccessHandler;
import com.server.pollingapp.service.CustomOauth2UserService;
import com.server.pollingapp.service.PollsUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Jos Wambugu
 * @since 12-04-2021
 */

@EnableWebSecurity
@Configuration
public class ApiSecurity extends WebSecurityConfigurerAdapter {
    final PollsUserDetailsService pollsUserDetailsService;

    final JwtFilter jwtFilter;

    private final CustomOauth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    public ApiSecurity(PollsUserDetailsService pollsUserDetailsService, JwtFilter jwtFilter, CustomOauth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.pollsUserDetailsService = pollsUserDetailsService;
        this.jwtFilter = jwtFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(pollsUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     * <p>
     * Any endpoint that requires defence against common vulnerabilities can be specified
     * here, including public ones. See {@link HttpSecurity#authorizeRequests} and the
     * `permitAll()` authorization rule for more details on public endpoints.
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //ALLOW HTTPS TRAFFIC AND TLS
        http.requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .csrf().disable()

                //STRICT AUTHORIZATION
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("api/v1/oauth2/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .and()

                //JWT-TOKEN-SESSION MANAGEMENT
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //OATH2
                .oauth2Login()
                .authorizationEndpoint()
                //->https://localhost:8443/api/v1/oauth2/authorize/{provider}?redirect_uri=<redirect_uri_after_login>.
                .baseUri("/api/v1/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/api/v1/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()

                //XSS PROTECTION
                .headers()
                .contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * Override this method to configure {@link WebSecurity}.For example, if you wish to
 ignore certain requests.<p>
     * Endpoints specified in this method will be ignored by Spring Security, meaning it
     * will not protect them from CSRF, XSS, Clickjacking, and so on.
     * <p>
     * Instead, if you want to protect endpoints against common vulnerabilities, then see
     * {@link #configure(HttpSecurity)} and the {@link HttpSecurity#authorizeRequests}
     * configuration method.
     *
     * @param web
     * @throws java.lang.Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator"
        );
    }

    /**
     * Override this method to expose the {@link AuthenticationManager} from
     * {@link #configure(AuthenticationManagerBuilder)} to be exposed as a Bean. For
     * example:
     *
     * <pre>
     * &#064;Bean(name name="myAuthenticationManager")
     * &#064;Override
     * public AuthenticationManager authenticationManagerBean() throws Exception {
     *     return super.authenticationManagerBean();
     * }
     * </pre>
     *
     * @return the {@link AuthenticationManager}
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    /**
     * Expose BcryptPasswordEncoder As A Bean
     * @return new BCryptPasswordEncoder()
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
     the authorization request.But, since our service is stateless, we can't save it in
     the session. We'll save the request in a Base64 encoded cookie instead.
     * @return 
   */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}
