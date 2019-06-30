import coursehunters.CourseHunters;
import dao.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import youtube.YouTube;

class CheckSite {
    private String site;

    CheckSite(String site) {
        this.site = site;
    }

    Parser validateSite() {
        Parser parser = null;

        final Logger logger = LoggerFactory.getLogger(CheckSite.class);
        if (site.matches(".*\\byoutube\\b.*")) {
            parser = new YouTube(site);
        } else if (site.matches(".*\\bcoursehunters\\b.*")) {
            parser = new CourseHunters(site);
        } else {
            logger.error("Site not defined");
        }

        return parser;
    }
}
