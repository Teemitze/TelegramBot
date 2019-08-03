package youtube;

import parsers.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Use YouTubeAPI
 */

@Deprecated
public class YouTube implements Parser {
    private final Logger logger = LoggerFactory.getLogger(YouTube.class);

    private String site;
    private Document doc;

    public YouTube(String site) {
        try {
            this.site = site;
            this.doc = Jsoup.connect(site).get();
        } catch (IOException e) {
            logger.error("Could not connect to the site.");
            e.printStackTrace();
        }
    }

    @Override
    public String parsingTitle() {
        return doc.getElementsByTag("h1").toggleClass("yt-simple-endpoint style-scope yt-formatted-string").text();
    }

    @Override
    public ArrayList<String> parsingContent() {
        ArrayList content = new ArrayList();
        for (Element e : doc.getElementsByTag("tr")) {
            content.add(e.attr("data-title"));
        }
        return content;
    }

    @Override
    public String getSite() {
        return site;
    }
}
