package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyAlbumDTO {
    private String id;
    private SpotifyImageAlbumDTO[] images;
    private String name;
    private String type;
}
