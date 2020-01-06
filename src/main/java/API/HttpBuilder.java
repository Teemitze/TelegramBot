package API;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;

import static API.trello.TrelloAPI.TRELLO_API;
import static API.youtube.YouTubeAPI.YOU_TUBE_API;

public class HttpBuilder {

    public static String trelloBuilder(String category, HashMap<String, String> parameters) {
        URIBuilder builder = null;
        try {
            builder = new URIBuilder(TRELLO_API + category);
            parameters.forEach(builder::addParameter);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String youTubeBuilder(HashMap<String, String> parameters) {
        URIBuilder builder = null;
        try {
            builder = new URIBuilder(YOU_TUBE_API);
            parameters.forEach(builder::addParameter);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
