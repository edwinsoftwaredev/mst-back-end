package com.edtech.plugtify.service;

import com.edtech.plugtify.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpotifyService {

    public SpotifyService() { }

    public User processAuthorizationCode(String authorizationCode) {

    }

}
