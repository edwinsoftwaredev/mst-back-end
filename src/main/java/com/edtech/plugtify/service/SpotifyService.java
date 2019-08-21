package com.edtech.plugtify.service;

import com.edtech.plugtify.config.ApplicationProperties;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
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

    public SpotifyService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public User processAuthorizationCode(AuthorizationCodeDTO authorizationCode) {

        String stringToBeEncode =
                applicationProperties.getSpotify().getClientId() + ":" + applicationProperties.getSpotify().getClientSecret();

        String value = "Basic " + Base64.getEncoder().encodeToString(stringToBeEncode.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", value);

        HttpEntity<AuthorizationCodeDTO> authorizationCodeDTOHttpEntity =
            new HttpEntity<>(authorizationCode, httpHeaders);

        this.restTemplate.postForEntity(this.SPOTIFY_TOKEN_END_POINT, authorizationCodeDTOHttpEntity, );
    }

}
