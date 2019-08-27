package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.service.SpotifyService;
import com.edtech.plugtify.service.dto.SpotifyTrackDTO;
import com.edtech.plugtify.service.dto.SpotifyUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SpotifyResource {

    private SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<SpotifyUserDTO> getCurrentUser() {
        return this.spotifyService.getCurrentUser();
    }

    @GetMapping("/recently-played")
    public ResponseEntity<SpotifyTrackDTO[]> getRecentlyPlayedTracks() {
        return this.spotifyService.getRecentlyPlayed();
    }

    @GetMapping("/suggested-playlist")
    public ResponseEntity<SpotifyTrackDTO[]> getSuggestedPlaylist() {
        return this.spotifyService.getSuggestedPlaylist();
    }

    @PostMapping("/add-playlist")
    public ResponseEntity<Void> addPlaylist(@RequestBody SpotifyTrackDTO[] tracks) {
        return this.spotifyService.addPlaylist(tracks);
    }

}
