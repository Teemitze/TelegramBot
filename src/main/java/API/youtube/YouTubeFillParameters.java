package API.youtube;

import java.util.HashMap;
import java.util.Map;

import static API.youtube.YouTubeServiceAPI.*;

public class YouTubeFillParameters {
    Map<String, String> fillParametersForYouTubePlaylist(String playlistId, String pageToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", PART);
        parameters.put("playlistId", playlistId);
        parameters.put("key", YOUTUBE_KEY_API);
        parameters.put("fields", FIELDS_ON_PLAYLIST);
        parameters.put("maxResults", MAX_RESULTS);
        parameters.put("pageToken", pageToken);
        return parameters;
    }

    Map<String, String> fillParametersForYouTubeTitlePlaylist(String playlistId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", PART);
        parameters.put("id", playlistId);
        parameters.put("key", YOUTUBE_KEY_API);
        parameters.put("fields", FIELDS_ON_TITLE_PLAYLIST);
        return parameters;
    }
}
