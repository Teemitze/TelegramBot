package API.youtube;

import configuration.Config;

import java.util.HashMap;
import java.util.Map;

import static API.youtube.YouTubeServiceAPI.*;

public class YouTubeFillParameters {

    Config config;
    String youtubeApiKey;

    public YouTubeFillParameters(Config config) {
        this.config = config;
        youtubeApiKey = config.youtubeApiKey;
    }

    Map<String, String> fillParametersForYouTubePlaylist(String playlistId, String pageToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", PART);
        parameters.put("playlistId", playlistId);
        parameters.put("key", youtubeApiKey);
        parameters.put("fields", FIELDS_ON_PLAYLIST);
        parameters.put("maxResults", MAX_RESULTS);
        parameters.put("pageToken", pageToken);
        return parameters;
    }

    Map<String, String> fillParametersForYouTubeTitlePlaylist(String playlistId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", PART);
        parameters.put("id", playlistId);
        parameters.put("key", youtubeApiKey);
        parameters.put("fields", FIELDS_ON_TITLE_PLAYLIST);
        return parameters;
    }
}
