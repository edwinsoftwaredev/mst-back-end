package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyPlaylistDTO {
    private String id;
    private String name;
    private String owner;
}
