package parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;


public class CourseHuntersHelper implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(CourseHuntersHelper.class);

    private String site;

    public CourseHuntersHelper(String site) {
        this.site = site;
        logger.info("Site: {}", site);
    }

    @Override
    public String parsingTitle(String siteUrl) {
        return getSiteDOM(site).getElementsByClass("hero-title").text();
    }

    @Nullable
    public Document getSiteDOM(String siteURL) {
        Document document = null;
        try {
            document = Jsoup.connect(siteURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public ArrayList<String> parsingContent() {
        ArrayList<String> result = new ArrayList<>();
        Elements content = getSiteDOM(site).getElementsByClass("lessons-name");
        if (!content.isEmpty()) {
            for (Element element : content) {
                result.add(element.text());
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