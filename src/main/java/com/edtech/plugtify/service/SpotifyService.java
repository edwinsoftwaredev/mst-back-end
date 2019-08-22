package com.edtech.plugtify.service;

import com.edtech.plugtify.config.ApplicationProperties;
import com.edtech.plugtify.domain.Token;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.repository.TokenRepository;
import com.edtech.plugtify.repository.UserRepository;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
import com.edtech.plugtify.service.dto.SpotifyUserDTO;
import com.edtech.plugtify.service.dto.TokenDTO;
import com.edtech.plugtify.web.rest.errors.InternalServerErrorException;
import com.edtech.plugtify.web.rest.errors.UserNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class SpotifyService {

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


    public void processAuthorizationCode(AuthorizationCodeDTO authorizationCode) throws Exception {

        HttpHeaders httpHeaders = this.getHttpHeaders();

        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();

        parameterMap.add("grant_type", authorizationCode.getGrant_type());
        parameterMap.add("code", authorizationCode.getCode());
        parameterMap.add("redirect_uri", authorizationCode.getRedirect_uri());

        HttpEntity<MultiValueMap<String, String>> parametersHttpEntity =
                new HttpEntity<MultiValueMap<String, String>>(parameterMap, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setMessageConverters(this.getMessageConverters());

        String SPOTIFY_TOKEN_END_POINT = "https://accounts.spotify.com/api/token";

        ResponseEntity<TokenDTO> newTokenResponse =
                restTemplate.postForEntity(SPOTIFY_TOKEN_END_POINT, parametersHttpEntity, TokenDTO.class);

        if (this.userService.getCurrentUser().isEmpty()) {
            throw new UserNotFoundException();
        }

        // get current user
        User actualUser = this.userService.getCurrentUser().get();

        // check if user have a token
        if (actualUser.getHasToken()) {
            // user have token; delete token
            actualUser.setToken(null);
            actualUser.setHasToken(false);
            this.userRepository.save(actualUser); // because the class is @Transactional we can do this
        }

        if(!newTokenResponse.hasBody()) {
            throw new Exception("newTokenResponse doesnt have body");
        }

        try {

            // the response body
            TokenDTO tokenDTORes = newTokenResponse.getBody();

            // set new token to user
            Token newToken = new Token();
            newToken.setAccess_token(tokenDTORes.getAccess_token());
            newToken.setExpires_in(tokenDTORes.getExpires_in());
            newToken.setRefresh_token(tokenDTORes.getRefresh_token());
            newToken.setScope(tokenDTORes.getScope());
            newToken.setToken_type(tokenDTORes.getToken_type());
            newToken.setLastUpdateTime(Timestamp.from(Instant.now()));

            actualUser.setToken(newToken);

            // set true for user has token
            actualUser.setHasToken(true);

            // update current user with its tokens
            this.userRepository.save(actualUser);

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // method to refresh access token
    public void refreshAccessToken() {
        if(this.userService.getCurrentUser().isEmpty()) {
            throw new UserNotFoundException();
        }

        User currentUser = this.userService.getCurrentUser().get();

        // check if user has token
        if(currentUser.getToken() == null) {
            return;
        }

        Token userToken = currentUser.getToken();

        Timestamp validTime = Timestamp
            .from(userToken
                .getLastUpdateTime()
                .toInstant()
                .plusSeconds(userToken.getExpires_in()));

        // check if validtime is > than the current time
        if(validTime.before(Timestamp.from(Instant.now()))) {
            // refresh access token

            HttpHeaders httpHeaders = this.getHttpHeaders();

            MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();
            parameterMap.add("grant_type", "refresh_token");
            parameterMap.add("refresh_token", userToken.getRefresh_token());

            HttpEntity<MultiValueMap<String, String>> parametersHttpEntity =
                new HttpEntity<MultiValueMap<String, String>>(parameterMap, httpHeaders);

            RestTemplate restTemplate = new RestTemplate();

            restTemplate.setMessageConverters(this.getMessageConverters());

            String spotifyTokenRefresherEndpoint = "https://accounts.spotify.com/api/token";

            ResponseEntity<TokenDTO> refreshedToken =
                restTemplate.postForEntity(spotifyTokenRefresherEndpoint, parametersHttpEntity, TokenDTO.class);

            if(refreshedToken.hasBody()) {

                userToken.setAccess_token(refreshedToken.getBody().getAccess_token());
                userToken.setScope(refreshedToken.getBody().getScope());
                userToken.setExpires_in(refreshedToken.getBody().getExpires_in());
                userToken.setToken_type(refreshedToken.getBody().getToken_type());
                userToken.setLastUpdateTime(Timestamp.from(Instant.now()));

                this.tokenRepository.save(userToken);

            } else {
                throw new InternalServerErrorException("response body is empty");
            }

        }

    }

    public SpotifyUserDTO getCurrentUser() {
        
    }

    public HttpHeaders getHttpHeaders() {
        String stringToBeEncoded =
                applicationProperties.getSpotify().getClientId() + ":" + applicationProperties.getSpotify().getClientSecret();

        String value = "Basic " + Base64.getEncoder().encodeToString(stringToBeEncoded.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", value);

        return httpHeaders;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter()); // Message converter for application/x-www-urlencoded -> Request
        converters.add(new MappingJackson2HttpMessageConverter()); // Message converter for application/JSON -> Response

        return converters;
    }
}
