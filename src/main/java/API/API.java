package API;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public interface API {
    default String addParameters(String siteAPI, HashMap<String, String> parameters) {
        String result = null;
        try {
            URIBuilder builder = new URIBuilder(siteAPI);
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                builder.addParameter(parameter.getKey(), parameter.getValue());
                result = siteAPI + builder.toString();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    };
}
