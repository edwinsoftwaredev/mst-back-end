package com.edtech.plugtify.service;

import com.edtech.plugtify.config.ApplicationProperties;
import com.edtech.plugtify.domain.Token;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.repository.TokenRepository;
import com.edtech.plugtify.repository.UserRepository;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
import com.edtech.plugtify.service.dto.TokenDTO;
import com.edtech.plugtify.web.rest.errors.UserNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public SpotifyService(
        ApplicationProperties applicationProperties,
        UserService userService,
        UserRepository userRepository,
        TokenRepository tokenRepository
    ) {
        this.applicationProperties = applicationProperties;
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public void processAuthorizationCode(AuthorizationCodeDTO authorizationCode) {

        String stringToBeEncode =
                applicationProperties.getSpotify().getClientId() + ":" + applicationProperties.getSpotify().getClientSecret();

        String value = "Basic " + Base64.getEncoder().encodeToString(stringToBeEncode.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", value);

        HttpEntity<AuthorizationCodeDTO> authorizationCodeDTOHttpEntity =
            new HttpEntity<>(authorizationCode, httpHeaders);

        ResponseEntity<Token> newTokenResponse =
            this.restTemplate.postForEntity(this.SPOTIFY_TOKEN_END_POINT, authorizationCodeDTOHttpEntity, Token.class);

        if (this.userService.getCurrentUser().isEmpty()) {
            throw new UserNotFoundException();
        }

        // get current user
        User actualUser = this.userService.getCurrentUser().get();

        // check if user have a token
        if(actualUser.getHasToken()) {
            // user have token; delete token
            this.tokenRepository.delete(actualUser.getToken()); // because the class is @Transactional we can do this
        }

        // set new token to user
        actualUser.setToken(newTokenResponse.getBody());

        // update current user with its tokens
        this.userRepository.save(actualUser);
    }
}
