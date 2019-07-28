package youtube;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class YouTubeAPI extends YouTube {

    private final Logger logger = LoggerFactory.getLogger(YouTubeAPI.class);

    private static final String YOU_TUBE_API = "https://www.googleapis.com/youtube/v3/playlistItems";
    private static final String PART = "snippet";
    private static final String KEY_API = "AIzaSyBDidy7us2sm7r8ewMJKzVJ2MuMS-aSMI4";
    private static final String FIELDS = "pageInfo,nextPageToken,items(snippet(title))";
    private static final String MAX_RESULTS = "50";

    private String site;

    @Override
    public String getSite() {
        return site;
    }

    public YouTubeAPI(String site) {
        super(site);
        this.site = site;
    }

    @Override
    public ArrayList<String> parsingContent() {
        String nextPageToken = "";

        YouTubeAPI youTubeAPI = new YouTubeAPI(site);
        logger.info("Site: {}", site);
        ArrayList<String> list = new ArrayList<>();

        logger.info("Title: {}", youTubeAPI.parsingTitle());

        while (true) {
            JSONObject pageJson = new JSONObject(youTubeAPI.api_Get(nextPageToken));
            JSONArray items = pageJson.getJSONArray("items");

            for (Object s : items) {
                JSONObject jsonObject = (JSONObject) s;
                String element = jsonObject.getJSONObject("snippet").getString("title");
                list.add(element);
                logger.info((list.indexOf(element) + 1) + ") " + element);
            }
            try {
                nextPageToken = pageJson.getString("nextPageToken");
            } catch (JSONException e) {
                break;
            }
        }
        return list;
    }


    public String api_Get(String pageToken) {
        String playlistId = site.trim().substring(38);
        HttpClient client = new HttpClient();
        String result = null;

        URIBuilder builder = null;
        try {
            builder = new URIBuilder(YOU_TUBE_API);
            builder.addParameter("part", PART);
            builder.addParameter("playlistId", playlistId);
            builder.addParameter("key", KEY_API);
            builder.addParameter("fields", FIELDS);
            builder.addParameter("maxResults", MAX_RESULTS);
            builder.addParameter("pageToken", pageToken);
        } catch (URISyntaxException e) {
            logger.error("Could not get the result using YouTube API");
            e.printStackTrace();
        }

        GetMethod method = new GetMethod(builder.toString());

        try {
            client.executeMethod(method);
            result = new String(method.getResponseBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
