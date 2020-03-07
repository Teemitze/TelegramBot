package API.youtube;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.Parser;

import java.util.ArrayList;

import static API.youtube.YouTubeAPI.MAX_RESULTS;

public class YouTubeHelper implements Parser {

    private final Logger logger = LoggerFactory.getLogger(YouTubeHelper.class);

    private final String site;

    YouTubeServiceAPI youTubeServiceAPI = new YouTubeServiceAPI();

    public YouTubeHelper(String site) {
        this.site = site;
    }

    @Override
    public String parsingTitle(String siteURL) {
        JSONObject jsonObject = new JSONObject(youTubeServiceAPI.getVideoPlaylistTitle(getPlaylistId(siteURL)));
        return getTitleFromJson(jsonObject);
    }

    public String getTitleFromJson(JSONObject jsonObject){
        return jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getJSONObject("localized").get("title").toString();
    }

    @Override
    public ArrayList<String> parsingContent() {
        String nextPageToken = "";

        ArrayList<String> videos = new ArrayList<>();

        JSONObject pageJson = new JSONObject(youTubeServiceAPI.getVideoPlaylist(getPlaylistId(site), nextPageToken));
        final String videoCount = pageJson.getJSONObject("pageInfo").get("totalResults").toString();
        final int pageCount = (int) Math.ceil(Double.parseDouble(videoCount) / Double.parseDouble(MAX_RESULTS));

        for (int i = pageCount; i > 0; i--) {
            JSONArray items = pageJson.getJSONArray("items");

            for (Object item : items) {
                JSONObject jsonObject = (JSONObject) item;
                String element = jsonObject.getJSONObject("snippet").getString("title");
                videos.add(element);
            }

            if (i >= 2) {
                nextPageToken = pageJson.getString("nextPageToken");
                pageJson = new JSONObject(youTubeServiceAPI.getVideoPlaylist(getPlaylistId(site), nextPageToken));
            }
        }
        return videos;
    }

    @Override
    public String getSite() {
        return site;
    }

    public String getPlaylistId(String siteURL) {
        return siteURL.trim().substring(38);
    }
}
