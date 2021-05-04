package com.server.pollingapp.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {

    @Value("${spring.social.twitter.appid}")
    String appId;

    @Value("${spring.social.twitter.appSecret}")
    String appSecret;

    @Value("${twitter.access.token}")
    String token;

    @Value("${twitter.access.tokenSecret}")
    String tokenSecret;



    @Bean
    public TwitterTemplate TwitterConfiguration(){

        return new TwitterTemplate(appId,appSecret,token,tokenSecret);
    }
}
