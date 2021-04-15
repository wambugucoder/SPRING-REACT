package com.server.pollingapp;

import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.service.PollStream;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Jos Wambugu
 * @since 12-04-2021
 * @apiNote <p>
 *     Its running ,don't do anything clever.
 * </p>
 */
@SpringBootApplication
@EnableCaching
@EnableEncryptableProperties
@EnableJpaAuditing
public class PollingappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollingappApplication.class, args);
    }

}
