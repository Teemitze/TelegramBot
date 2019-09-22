package API.youtube;

import java.util.HashMap;

import static API.youtube.YouTubeAPI.*;

public class YouTubeFillParameters {
    HashMap<String, String> fillParametersForYouTubePlaylist(String playlistId, String pageToken) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("part", PART);
        parameters.put("playlistId", playlistId);
        parameters.put("key", KEY_API);
        parameters.put("fields", FIELDS);
        parameters.put("maxResults", MAX_RESULTS);
        parameters.put("pageToken", pageToken);
        return parameters;
    }
}
