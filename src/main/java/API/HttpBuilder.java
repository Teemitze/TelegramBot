package API;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Map;

public class HttpBuilder {
    public static String getHttpBuilder(String linkApi, Map<String, String> parameters) {
        URIBuilder builder = null;
        try {
            builder = new URIBuilder(linkApi);
            parameters.forEach(builder::addParameter);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
