package API.youtube;

import configuration.Config;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static API.HttpBuilder.getHttpBuilder;

public class YouTubeServiceAPI implements YouTubeAPI {

    private final Logger logger = LoggerFactory.getLogger(YouTubeServiceAPI.class);

    Config config = Config.loadConfig();

    YouTubeFillParameters youTubeFillParameters = new YouTubeFillParameters(config);

    public String getVideoPlaylist(String playlistId, String pageToken) {
        String result = null;
        try {
            String URI = getHttpBuilder(YOUTUBE_PLAYLIST_API, youTubeFillParameters.fillParametersForYouTubePlaylist(playlistId, pageToken));
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(URI);
            client.executeMethod(method);
            result = new String(method.getResponseBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getVideoPlaylistTitle(String playlistId) {
        String result = null;
        try {
            String URI = getHttpBuilder(YOUTUBE_TITLE_API, youTubeFillParameters.fillParametersForYouTubeTitlePlaylist(playlistId));
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(URI);
            client.executeMethod(method);
            result = new String(method.getResponseBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
