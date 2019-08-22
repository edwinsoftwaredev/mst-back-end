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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // spotify require this type of encoder
        httpHeaders.add("Authorization", value);

        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();

        parameterMap.add("grant_type", authorizationCode.getGrant_type());
        parameterMap.add("code", authorizationCode.getCode());
        parameterMap.add("redirect_uri", authorizationCode.getRedirect_uri());

        HttpEntity<MultiValueMap<String, String>> parametersHttpEntity =
            new HttpEntity<MultiValueMap<String, String>>(parameterMap, httpHeaders);

        this.restTemplate.setMessageConverters(this.getMessageConverters());

        ResponseEntity<String> newTokenResponse =
            this.restTemplate.postForEntity(this.SPOTIFY_TOKEN_END_POINT, parametersHttpEntity, String.class);

        if (this.userService.getCurrentUser().isEmpty()) {
            throw new UserNotFoundException();
        }

        // get current user
        // User actualUser = this.userService.getCurrentUser().get();

        // check if user have a token
        /*if(actualUser.getHasToken()) {
            // user have token; delete token
            actualUser.setToken(null);
            actualUser.setHasToken(false);
            this.userRepository.save(actualUser); // because the class is @Transactional we can do this
        }*/

        // set new token to user
        // actualUser.setToken(newTokenResponse.getBody());

        // set true for user has token
        // actualUser.setHasToken(true);

        System.out.println("-----TEST-----");
        System.out.println("body: " + newTokenResponse.getBody().toString());
        System.out.println("status: " + newTokenResponse.getStatusCodeValue());

        // update current user with its tokens
        // this.userRepository.save(actualUser);
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter()); // Message converter for application/x-www-urlencoded -> Request
        converters.add(new MappingJackson2HttpMessageConverter()); // Message converter for application/JSON -> Response

        return converters;
    }
}
