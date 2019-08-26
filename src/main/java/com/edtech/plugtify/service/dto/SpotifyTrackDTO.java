package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyTrackDTO {
    private SpotifyAlbumDTO album;
    private SpotifyArtistDTO[] artists;
    private int duration_ms;
    private SpotifyExternalUrlDTO external_urls;
    private String id;
    private Boolean is_playable;
    private String name;
    private int popularity;
    private String preview_url;
    private String type;
    private SpotifyAudioFeaturesDTO audio_feature;
}
