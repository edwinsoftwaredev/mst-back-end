package com.edtech.plugtify.service;

/**
 * Spotify constants
 */
final class SpotifyConstants {

    static final String URL_EXCHANGE_TOKEN = "https://accounts.spotify.com/api/token";
    static final String URL_REFRESH_TOKEN = "https://accounts.spotify.com/api/token";
    static final String URL_CURRENT_USER = "https://api.spotify.com/v1/me";
    static final String URL_RECENTLY_PLAYED = "https://api.spotify.com/v1/me/player/recently-played";
    static final String URL_TRACKS = "https://api.spotify.com/v1/tracks";
    static final String URL_FEATURES_TRACKS = "https://api.spotify.com/v1/audio-features";
    static final String URL_RECOMMENDATIONS = "https://api.spotify.com/v1/recommendations";
    static final String URL_CREATE_PLAYLIST = "https://api.spotify.com/v1/me/playlists";
    static final String URL_REPLACE_PLAYLIST = "https://api.spotify.com/v1/playlists/{playlist_id}/tracks";
    static final String URL_UNFOLLOW_PLAYLIST = "https://api.spotify.com/v1/playlists/{playlist_id}/followers";
    private SpotifyConstants() {}

}
