package API.youtube;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.YouTube;

import java.io.IOException;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

import static API.HttpBuilder.youTubeBuilder;

public class YouTubeAPI extends YouTube {

    private final Logger logger = LoggerFactory.getLogger(YouTubeAPI.class);

    public static final String YOU_TUBE_API = "https://www.googleapis.com/youtube/v3/playlistItems";
    public static final String PART = "snippet";
    public static String KEY_API;
    public static final String FIELDS = "pageInfo,nextPageToken,items(snippet(title))";
    public static final String MAX_RESULTS = "50";

    private String site;

    @Override
    public String getSite() {
        return site;
    }

    public YouTubeAPI(String site) {
        super(site);
        this.site = site;
        try {
            Properties property = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            KEY_API = property.getProperty("youtube.key");
        } catch (Exception e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    @Override
    public ArrayList<String> parsingContent() {
        String nextPageToken = "";

        YouTubeAPI youTubeAPI = new YouTubeAPI(site);
        logger.info("Site: {}", site);
        ArrayList<String> list = new ArrayList<>();

        logger.info("Title: {}", youTubeAPI.parsingTitle());
        int i = 1;
        while (true) {
            JSONObject pageJson = new JSONObject(youTubeAPI.api_Get(nextPageToken));
            JSONArray items = pageJson.getJSONArray("items");

            for (Object s : items) {
                JSONObject jsonObject = (JSONObject) s;
                String element = jsonObject.getJSONObject("snippet").getString("title");
                list.add(i + ") " + element);
                logger.info(i + ") " + element);
                i++;
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
        String result = null;
        try {
            String playlistId = site.trim().substring(38);
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(youTubeBuilder(new YouTubeFillParameters().fillParametersForYouTubePlaylist(playlistId, pageToken)));
            client.executeMethod(method);
            result = new String(method.getResponseBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
