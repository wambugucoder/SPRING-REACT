package com.server.pollingapp.security;


import com.server.pollingapp.service.PollsUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Jos Wambugu
 * @since 12-04-2021
 */

@EnableWebSecurity
@Configuration
public class ApiSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    PollsUserDetailsService pollsUserDetailsService;

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
     * Any endpoint that requires defense against common vulnerabilities can be specified
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
                .antMatchers("/api/v1/**").permitAll();

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
}
