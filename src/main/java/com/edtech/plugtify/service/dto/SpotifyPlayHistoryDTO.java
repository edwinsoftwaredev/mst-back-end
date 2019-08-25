package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class SpotifyPlayHistoryDTO {
    private SpotifyTrackDTO track;
    private Timestamp played_at;
}
