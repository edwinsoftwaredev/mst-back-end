package com.edtech.plugtify.service;

import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.service.dto.AuthorizationCodeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class SpotifyService {

    private RestTemplate restTemplate = new RestTemplate();

    public SpotifyService() { }

    public User processAuthorizationCode(AuthorizationCodeDTO authorizationCode) {
        this.restTemplate.post
    }

}
