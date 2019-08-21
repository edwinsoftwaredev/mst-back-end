package com.edtech.plugtify.service;

import com.edtech.plugtify.config.ApplicationProperties;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
import com.edtech.plugtify.service.dto.TokenDTO;
import com.edtech.plugtify.web.rest.errors.UserNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Transactional
public class SpotifyService {

    private final String SPOTIFY_TOKEN_END_POINT = "https://accounts.spotify.com/api/token";

    private RestTemplate restTemplate = new RestTemplate();
    private ApplicationProperties applicationProperties;
    private UserService userService;

    public SpotifyService(
        ApplicationProperties applicationProperties,
        UserService userService
    ) {
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    public User processAuthorizationCode(AuthorizationCodeDTO authorizationCode) {

        String stringToBeEncode =
                applicationProperties.getSpotify().getClientId() + ":" + applicationProperties.getSpotify().getClientSecret();

        String value = "Basic " + Base64.getEncoder().encodeToString(stringToBeEncode.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", value);

        HttpEntity<AuthorizationCodeDTO> authorizationCodeDTOHttpEntity =
            new HttpEntity<>(authorizationCode, httpHeaders);

        TokenDTO tokenDTO =
            this.restTemplate.postForObject(this.SPOTIFY_TOKEN_END_POINT, authorizationCodeDTOHttpEntity, TokenDTO.class);

        if (this.userService.getCurrentUser().isEmpty()) {
            throw new UserNotFoundException();
        }

        User actualUser = this.userService.getCurrentUser().get();


    }
}
