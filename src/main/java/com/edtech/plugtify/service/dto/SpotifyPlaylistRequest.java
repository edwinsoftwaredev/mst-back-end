package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyPlaylistRequest {

    private String name;
    private String description;

    public SpotifyPlaylistRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
