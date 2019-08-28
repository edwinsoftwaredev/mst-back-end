package com.edtech.plugtify.service;

import com.edtech.plugtify.domain.Token;
import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.repository.UserRepository;
import com.edtech.plugtify.service.dto.*;
import com.edtech.plugtify.web.rest.errors.InternalServerErrorException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class BackgroundProcesses {

    private SpotifyService spotifyService;
    private UserRepository userRepository;

    public BackgroundProcesses(
            SpotifyService spotifyService,
            UserRepository userRepository

    ) {
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedDelay = 3600000)
    private void updatePlaylists() {
        System.out.println("updatePlaylists() is running");

        // token updated 1hr ago are updated
        Set<User> users = this.userRepository.findAllTokensByDay(Timestamp.from(Instant.now().minusMillis(3600000)));

        users.removeIf(user -> user.getPlaylistId() == null);

        if(users.size() != 0) {
            users.forEach(this::replacePlaylist);
        }
    }

    private void replacePlaylist(User user) {
        Token userToken = user.getToken();

        if(this.spotifyService.isTokenExpired(userToken)) {
            this.spotifyService.refreshAccessToken(userToken);
        }

        ResponseEntity<SpotifyTrackDTO[]> tracksPlayed = this.getRecentlyPlayed(userToken);

        ResponseEntity<SpotifyTrackDTO[]> tracksSuggested = this.getSuggestedPlaylist(tracksPlayed, userToken);

        if(tracksSuggested.hasBody() && tracksSuggested.getStatusCodeValue() == 200) {
            this.spotifyService.replaceTrackPlaylist(tracksSuggested.getBody(), user.getPlaylistId(), userToken);
        }
    }

    /**
     * Get the recently played tracks by the user
     * @return response
     */
    @SuppressWarnings("unchecked")
    private ResponseEntity<SpotifyTrackDTO[]> getRecentlyPlayed(Token userToken) {

        HttpHeaders httpHeaders = this.spotifyService.getHttpHeaders(userToken);

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(SpotifyConstants.URL_RECENTLY_PLAYED)
                .queryParam("limit", 50); // query parameter to get the last 50 played tracks

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        // first, we get the last 50 played tracks -> tracks objects are simplified
        ResponseEntity<SpotifyItemsDTO> responsePlayHistory =
                (ResponseEntity<SpotifyItemsDTO>) this.spotifyService.getClientResponseEntity(this.spotifyService.getRequests(urlBuilder.toUriString(), SpotifyItemsDTO.class, httpEntity));

        if(!responsePlayHistory.hasBody()) {
            throw new InternalServerErrorException("There are not Recently played Tracks for this user");
        }

        // Second, we get the full objects for each track recently played

        // --> getting the ids from responsePlayHistory for each track and save them in a String variable
        String ids = Arrays.stream(Objects.requireNonNull(responsePlayHistory.getBody()).getItems()).map(historyObject -> historyObject.getTrack().getId()).collect(Collectors.joining(",")); // separating each id with a ,

        // Third, we get the full track object for each id in the ids variable

        urlBuilder = UriComponentsBuilder.fromHttpUrl(SpotifyConstants.URL_TRACKS)
                .queryParam("ids", ids);

        if(ids.length() == 0) {
            throw new InternalServerErrorException("User doesnt has recently played track");
        }

        ResponseEntity<SpotifyTrackArrayDTO> responseTracks =
                (ResponseEntity<SpotifyTrackArrayDTO>) this.spotifyService.getClientResponseEntity(this.spotifyService.getRequests(urlBuilder.toUriString(), SpotifyTrackArrayDTO.class, httpEntity));

        if(!responseTracks.hasBody()) {
            throw new InternalServerErrorException("There was a problem getting the full object for each tracks");
        }

        urlBuilder = UriComponentsBuilder.fromHttpUrl(SpotifyConstants.URL_FEATURES_TRACKS)
                .queryParam("ids", ids);

        // Forth, we get the features of each track by id
        ResponseEntity<SpotifyAudioFeatureArrayDTO> responseTracksFeatures =
                (ResponseEntity<SpotifyAudioFeatureArrayDTO>) this.spotifyService.getClientResponseEntity(this.spotifyService.getRequests(urlBuilder.toUriString(), SpotifyAudioFeatureArrayDTO.class, httpEntity));

        SpotifyTrackDTO[] tracks = Arrays.stream(Objects.requireNonNull(responseTracks.getBody()).getTracks()).peek(track -> {
            // merge the track with its features

            SpotifyAudioFeaturesDTO audioFeatures = Arrays.stream(Objects.requireNonNull(responseTracksFeatures.getBody()).getAudio_features())
                    .filter(trackFeature -> trackFeature.getId().equals(track.getId())).collect(Collectors.toList()).get(0);

            track.setAudio_feature(audioFeatures);

        }).toArray(SpotifyTrackDTO[]::new);

        return new ResponseEntity<>(tracks, HttpStatus.OK) ;
    }

    /**
     * Method to get recommended tracks
     * @return ResponseEntity<SpotifyTrackDTO[]>
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<SpotifyTrackDTO[]> getSuggestedPlaylist(ResponseEntity<SpotifyTrackDTO[]> tracksPlayed, Token userToken) {
        float acousticness = 0.0f;
        float danceability = 0.0f;
        float energy = 0.0f;
        float instrumentalness = 0.0f;
        float liveness = 0.0f;
        float speechiness = 0.0f;
        float valence = 0.0f;
        int popularity = 0; // --> 0-100 value

        // get average of each audio feature

        if(!tracksPlayed.hasBody()) {
            throw new InternalServerErrorException("Can't get recently played tracks");
        }

        int cantTracks = Objects.requireNonNull(tracksPlayed.getBody()).length;

        for (SpotifyTrackDTO track: tracksPlayed.getBody()) {
            acousticness = acousticness + track.getAudio_feature().getAcousticness();
            danceability = danceability + track.getAudio_feature().getDanceability();
            energy = energy + track.getAudio_feature().getEnergy();
            instrumentalness = instrumentalness + track.getAudio_feature().getInstrumentalness();
            liveness = liveness + track.getAudio_feature().getLiveness();
            speechiness = speechiness + track.getAudio_feature().getSpeechiness();
            valence = valence + track.getAudio_feature().getValence();
            popularity = popularity + track.getPopularity();
        }

        acousticness = acousticness / cantTracks;
        danceability = danceability / cantTracks;
        energy = energy / cantTracks;
        instrumentalness = instrumentalness / cantTracks;
        liveness = liveness / cantTracks;
        speechiness = speechiness / cantTracks;
        valence = valence / cantTracks;
        popularity = popularity / cantTracks;

        // get 5 random number between 0 and 49 to get the tracks in those indexs, and get the ids
        Random random = new Random();

        String seedTracks;
        Set<String> seedsTracks = new HashSet<>();

        if(tracksPlayed.getBody().length < 10) {
            seedTracks = tracksPlayed.getBody()[0].getId();
        } else {
            do {
                seedsTracks.add(tracksPlayed.getBody()[random.nextInt(tracksPlayed.getBody().length)].getId());
            } while(seedsTracks.size() < 5);

            seedTracks = String.join(",", seedsTracks);
        }

        HttpHeaders httpHeaders = this.spotifyService.getHttpHeaders(userToken);

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(SpotifyConstants.URL_RECOMMENDATIONS)
                .queryParam("limit", 50)
                .queryParam("seed_tracks", seedTracks)
                .queryParam("target_acousticness", acousticness)
                .queryParam("target_danceability", danceability)
                .queryParam("target_energy", energy)
                .queryParam("target_instrumentalness", instrumentalness)
                .queryParam("target_liveness", liveness)
                .queryParam("target_speechiness", speechiness)
                .queryParam("target_valence", valence)
                .queryParam("min_popularity", popularity);

        HttpEntity<MultiValueMap<String, String>> httpEntity =
                new HttpEntity<>(httpHeaders);

        // array of tracks simplified
        ResponseEntity<SpotifyTrackArrayDTO> arrayTracksSimplified =
                (ResponseEntity<SpotifyTrackArrayDTO>) this.spotifyService.getClientResponseEntity(this.spotifyService.getRequests(urlBuilder.toUriString(), SpotifyTrackArrayDTO.class, httpEntity));

        List<SpotifyTrackDTO> listTracksSimplified = Arrays.stream(Objects.requireNonNull(arrayTracksSimplified.getBody()).getTracks())
                .collect(Collectors.toList());

        // removing repeated tracks
        Arrays.stream(tracksPlayed.getBody()).forEach(spotifyTrackDTO -> listTracksSimplified.removeIf(track -> track.getId().equals(spotifyTrackDTO.getId())));

        String ids = Arrays.stream(listTracksSimplified.toArray(SpotifyTrackDTO[]::new))
                .map(SpotifyTrackDTO::getId)
                .collect(Collectors.joining(","));

        // array of full object tracks
        urlBuilder = UriComponentsBuilder.fromHttpUrl(SpotifyConstants.URL_TRACKS)
                .queryParam("ids", ids);

        ResponseEntity<SpotifyTrackArrayDTO> responseTracks =
                (ResponseEntity<SpotifyTrackArrayDTO>) this.spotifyService.getClientResponseEntity(this.spotifyService.getRequests(urlBuilder.toUriString(), SpotifyTrackArrayDTO.class, httpEntity));

        if(!responseTracks.hasBody()) {
            throw new InternalServerErrorException("There was a problem getting the full object for each tracks");
        }

        return new ResponseEntity<>(Arrays.stream(Objects.requireNonNull(responseTracks.getBody()).getTracks()).toArray(SpotifyTrackDTO[]::new), HttpStatus.OK) ;

    }

}
