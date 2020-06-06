package parsers.api.youtube;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.api.ApiParserUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static parsers.api.ApiParserUtil.getHttpBuilder;

public class YouTubeParserUtil {

    private static final Logger LOG = LoggerFactory.getLogger(YouTubeParserUtil.class);

    public static final String YOUTUBE_PLAYLIST_API = "https://www.googleapis.com/youtube/v3/playlistItems";
    public static final String YOUTUBE_TITLE_API = "https://www.googleapis.com/youtube/v3/playlists";
    private static final String PART = "snippet";
    private static final String FIELDS_ON_PLAYLIST = "pageInfo,nextPageToken,items(snippet(title))";
    private static final String FIELDS_ON_TITLE_PLAYLIST = "items(snippet(localized(title)))";
    public static final String MAX_RESULTS = "50";
    private static final String YOUTUBE_API_KEY = ApiParserUtil.readApiKey("youtube.key");

    public static List<NameValuePair> fillParametersForYouTubePlaylist(String playlistId, String pageToken) {
        return List.of(
                new BasicNameValuePair("part", PART),
                new BasicNameValuePair("playlistId", playlistId),
                new BasicNameValuePair("key", YOUTUBE_API_KEY),
                new BasicNameValuePair("fields", FIELDS_ON_PLAYLIST),
                new BasicNameValuePair("maxResults", MAX_RESULTS),
                new BasicNameValuePair("pageToken", pageToken));
    }

    public static List<NameValuePair> fillParametersForYouTubeTitlePlaylist(String playlistId) {
        return List.of(
                new BasicNameValuePair("part", PART),
                new BasicNameValuePair("id", playlistId),
                new BasicNameValuePair("key", YOUTUBE_API_KEY),
                new BasicNameValuePair("fields", FIELDS_ON_TITLE_PLAYLIST));
    }

    public static Optional<JSONObject> getVideoPlaylistJSON(String playlistId, String pageToken) {
        try {
            final Optional<String> URI = getHttpBuilder(YOUTUBE_PLAYLIST_API, fillParametersForYouTubePlaylist(playlistId, pageToken))
                    .map(URIBuilder::toString);
            if (URI.isPresent()) {
                final GetMethod getMethod = new GetMethod(URI.get());
                new HttpClient().executeMethod(getMethod);
                return Optional.of(new JSONObject(new String(getMethod.getResponseBodyAsStream().readAllBytes())));
            }
        } catch (IOException e) {
            LOG.error("Couldn't get playlist {}", playlistId, e);
        }
        return Optional.empty();
    }

    public static Optional<JSONObject> getPlaylistTitleJSON(String playlistId) {
        try {
            final Optional<String> URI = getHttpBuilder(YOUTUBE_TITLE_API, fillParametersForYouTubeTitlePlaylist(playlistId))
                    .map(URIBuilder::toString);
            if (URI.isPresent()) {
                final GetMethod getMethod = new GetMethod(URI.get());
                new HttpClient().executeMethod(getMethod);
                return Optional.of(new JSONObject(new String(getMethod.getResponseBodyAsStream().readAllBytes())));
            }
        } catch (IOException e) {
            LOG.error("Couldn't get title from playlist {}", playlistId, e);
        }
        return Optional.empty();
    }
}
