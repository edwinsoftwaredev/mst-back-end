package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.service.SpotifyService;
import com.edtech.plugtify.service.dto.SpotifyUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SpotifyResource {

    SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<SpotifyUserDTO> getCurrentUser() {
        return this.spotifyService.getCurrentUser();
    }
}
