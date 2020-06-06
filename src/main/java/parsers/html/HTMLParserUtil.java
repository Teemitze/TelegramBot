package parsers.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

class HTMLParserUtil {
    public static final Logger LOG = LoggerFactory.getLogger(HTMLParserUtil.class);

    public static Optional<Document> getSiteDOM(String url) {
        Optional<Document> document = Optional.empty();
        try {
            document = Optional.of(Jsoup.connect(url).get());
        } catch (Exception e) {
            LOG.error("Couldn't get DOM from site {}", url, e);
        }
        return document;
    }
}
