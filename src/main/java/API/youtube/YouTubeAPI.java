package API.youtube;

public interface YouTubeAPI {
    String YOUTUBE_PLAYLIST_API = "https://www.googleapis.com/youtube/v3/playlistItems";
    String YOUTUBE_TITLE_API = "https://www.googleapis.com/youtube/v3/playlists";
    String PART = "snippet";
    String FIELDS_ON_PLAYLIST = "pageInfo,nextPageToken,items(snippet(title))";
    String FIELDS_ON_TITLE_PLAYLIST = "items(snippet(localized(title)))";
    String MAX_RESULTS = "50";
}
