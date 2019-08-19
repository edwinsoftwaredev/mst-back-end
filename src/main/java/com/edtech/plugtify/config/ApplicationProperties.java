package com.edtech.plugtify.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /**
     * variable for the applicacion properties file
     */
    private final Spotify spotify = new Spotify();

    public Spotify getSpotify() {
        return spotify;
    }

    /**
     * ObjectMapper Bean configuration requiered by Zalando Problem.
     * Zalando Problem is a small library to let customize APIs errors.
     * implementation: https://www.baeldung.com/problem-spring-web
    **/
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }

    public static class Spotify {

        private String clientId;
        private String clientSecret;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }

}
