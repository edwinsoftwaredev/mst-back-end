package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.config.ApplicationProperties;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.service.SpotifyService;
import com.edtech.plugtify.service.UserService;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    private ApplicationProperties applicationProperties;
    private SpotifyService spotifyService;

    public AuthenticationResource(
        ApplicationProperties applicationProperties,
        SpotifyService spotifyService
    ) {
        this.applicationProperties = applicationProperties;
        this.spotifyService = spotifyService;
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

    /**
     * Method to get the spotify tokens and process the auth code
     * @param authorizationCodeDTO authorization code to get the tokens
     * @return Spotify user
     */
    @PostMapping("/authorization-code")
    public User processAuthorization(@Valid @RequestBody AuthorizationCodeDTO authorizationCodeDTO) {
        return this.spotifyService.processAuthorizationCode(authorizationCodeDTO);
    }
}
