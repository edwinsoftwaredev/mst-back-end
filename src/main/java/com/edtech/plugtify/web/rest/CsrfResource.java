package com.edtech.plugtify.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class CsrfResource {

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {

        System.out.println("-----RestTemplate Result------");

        try {
            // testing request with egress default
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> res = restTemplate.getForEntity("https://maps.googleapis.com/maps/api/timezone/json?location=39.6034810,-119.6822510&timestamp=1331161200", String.class);

            System.out.println(res.getBody());
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        return csrfToken;
    }

}
