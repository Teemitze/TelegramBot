package parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class CourseHunters implements Parser {
    private final Logger logger = LoggerFactory.getLogger(CourseHunters.class);

    private String site;
    private Document doc;

    public CourseHunters(String site) {
        try {
            this.site = site;
            this.doc = Jsoup.connect(site).get();
            logger.info("Site: {}", site);
        } catch (UnknownHostException e) {
            logger.error("Could not connect to the site.");
        } catch (IOException e) {
            logger.error("Input output error!");
        }
    }

    @Override
    public String parsingTitle() {
        String title = doc.getElementsByClass("hero-title").text();
        if (title != null) {
            logger.info("Title: {}", title);
        } else {
            logger.error("Title is empty!");
        }
        return title;
    }

    @Override
    public ArrayList<String> parsingContent() {
        ArrayList<String> result = new ArrayList<>();
        Elements content = doc.getElementsByClass("lessons-name");
        if (!content.isEmpty()) {
            for (Element element : content) {
                result.add((content.indexOf(element) + 1) + ") " + element.text());
                logger.info((content.indexOf(element) + 1) + ") " + element.text());
            }
        } else {
            logger.error("Content is empty!");
        }
        return result;
    }

    @Override
    public String getSite() {
        return site;
    }
}