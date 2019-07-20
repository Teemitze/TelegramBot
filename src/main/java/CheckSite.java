import coursehunters.CourseHunters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import youtube.YouTubeAPI;

class CheckSite {
    private String site;

    CheckSite(String site) {
        this.site = site;
    }

    void validateSite() {
        final Logger logger = LoggerFactory.getLogger(CheckSite.class);
        if (site.matches(".*\\byoutube\\b.*")) {
            YouTubeAPI youTubeAPI = new YouTubeAPI(site);
            youTubeAPI.writeFile(youTubeAPI.parsingContent(), site);
        } else if (site.matches(".*\\bcoursehunters\\b.*")) {
            CourseHunters courseHunters = new CourseHunters(site);
            courseHunters.writeFile(courseHunters.parsingContent(), site);
        } else {
            logger.error("Site not defined");
        }
    }
}