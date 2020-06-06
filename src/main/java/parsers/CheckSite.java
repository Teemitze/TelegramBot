package parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.api.youtube.YouTubeParser;
import parsers.html.CourseHuntersParser;

public class CheckSite {
    private final Logger logger = LoggerFactory.getLogger(CheckSite.class);
    private String site;

    public CheckSite(String site) {
        this.site = site;
    }

    public Parser distributorSite() {

        Parser result = null;

        if (site.matches(".*\\byoutube\\b.*")) {
            result = new YouTubeParser(site);
        } else if (site.matches(".*\\bcoursehunter\\b.*")) {
            result = new CourseHuntersParser(site);
        } else {
            logger.error("Site not defined");
        }

        return result;
    }
}