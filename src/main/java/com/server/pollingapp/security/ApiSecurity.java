package com.server.pollingapp.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Jos Wambugu
 * @since 12-04-2021
 * @implNote Configuring Api Security i.e Authorization,TLS,jwts,crsf ,sessions which have to be stateless
 * and contentSecurityPolicy.
 * @see WebSecurityConfigurerAdapter
 */

@EnableWebSecurity
@Configuration
public class ApiSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

               //ALLOW HTTPS TRAFFIC
        http.requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .csrf().disable()

                //STRICT AUTHORIZATION
                .authorizeRequests()
                .antMatchers("/api/v1/**").permitAll();

    }
}
