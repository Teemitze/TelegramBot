package coursehunters;

import dao.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;


public class CourseHunters implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(CourseHunters.class);

    private String site;
    private Document doc;

    public CourseHunters(String site) {
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
        return doc.getElementsByClass("hero-title").text();
    }

    @Override
    public ArrayList<String> parsingContent() {
        ArrayList content = new ArrayList();
        for (Element e : doc.getElementsByClass("lessons-name")) {
            content.add(e.text());
        }
        return content;
    }
}