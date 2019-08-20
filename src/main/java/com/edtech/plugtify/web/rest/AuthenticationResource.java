package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.config.ApplicationProperties;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    private ApplicationProperties applicationProperties;

    public AuthenticationResource(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Method to get principal when user send it's credentials in login form
     * @param principal user logged
     * @return principal
     */
    @GetMapping("/user")
    public ResponseEntity<Principal> getAuthenticatedUsed(Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    /**
     * Method to get the Spotify client Id
     * @return the Spotify Client Id
     */
    @GetMapping("/client-id")
    public ResponseEntity<String> getSpotifyClientId() {

        final String clientIdJSON = "{"+ "\""+"clientId"+"\"" +":"+ "\""+ this.applicationProperties.getSpotify().getClientId() + "\"" + "}";

        return new ResponseEntity<String>(clientIdJSON, HttpStatus.OK);
    }
}
