package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyAudioFeatureDTO {
    private float acousticness;
    private float danceability;
    private float energy;
    private String id;
    private float instrumentalness;
    private float liveness;
    private float speechiness;
    private float tempo;
    private String type;
    private float valence;
}
