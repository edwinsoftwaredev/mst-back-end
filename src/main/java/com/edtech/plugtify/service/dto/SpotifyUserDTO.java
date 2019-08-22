package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyUserDTO {

    private String country;
    private String display_name;
    private String email;
    private String href;
    private String id;
    private SpotifyUserImage[] images;
    private String type;
    
}
