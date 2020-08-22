package parsers.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ApiParserUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ApiParserUtil.class);

    public static Optional<URIBuilder> getHttpBuilder(String linkApi, List<NameValuePair> nameValuePairs) {
        try {
            URIBuilder builder = new URIBuilder(linkApi);
            builder.addParameters(nameValuePairs);
            return Optional.of(builder);
        } catch (URISyntaxException e) {
            LOG.error("Couldn't create URIBuilder {}", linkApi, e);
            return Optional.empty();
        }
    }

    public static String readApiKey(final String keyName) {
        try (final InputStream is = ApiParserUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            final Properties property = new Properties();
            property.load(is);
            return property.getProperty(keyName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
