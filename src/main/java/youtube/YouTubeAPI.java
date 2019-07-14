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

public class YouTubeAPI extends YouTube{

    private static final Logger logger = LoggerFactory.getLogger(YouTubeAPI.class);

    private static final String YOU_TUBE_API = "https://www.googleapis.com/youtube/v3/playlistItems";
    private static final String PART = "snippet";
    private static final String KEY_API = "AIzaSyBDidy7us2sm7r8ewMJKzVJ2MuMS-aSMI4";
    private static final String FIELDS = "pageInfo,nextPageToken,items(snippet(title))";
    private static final String MAX_RESULTS = "50";


     String site;


    public YouTubeAPI(String site) {
        super(site);
        this.site = site;
    }

    @Override
    public ArrayList<String> parsingContent() {
        String nextPageToken = "";


        ArrayList<String> list = new ArrayList<>();
        do {
            JSONObject jsonObject = new JSONObject(new YouTubeAPI(site).api_Get(nextPageToken));
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (Object s : jsonArray) {
                JSONObject jsonObject1 = (JSONObject) s;
                JSONObject jsonObject2 = (JSONObject) jsonObject1.get("snippet");
                list.add(jsonObject2.get("title").toString());
            }
            try {
                nextPageToken = jsonObject.getString("nextPageToken");
            } catch (JSONException e) {
                break;
            }


        } while (!nextPageToken.equals(""));

        return list;
    }


    public String api_Get (String pageToken) {
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
