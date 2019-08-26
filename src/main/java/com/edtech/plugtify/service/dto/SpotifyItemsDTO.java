package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is just a wrapper fo SpotifyPlayHistoryDTO
 */

@Getter
@Setter
@NoArgsConstructor
public class SpotifyItemsDTO {
    private SpotifyPlayHistoryDTO[] items;
}
