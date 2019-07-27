package dao;

import coursehunters.CourseHunters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trello.TrelloAPI;
import youtube.YouTubeAPI;

import java.util.ArrayList;

public class CheckSite {
    private String site;

    public CheckSite(String site) {
        this.site = site;
    }

    public void validateSite() {
        final Logger logger = LoggerFactory.getLogger(CheckSite.class);
        if (site.matches(".*\\byoutube\\b.*")) {
            YouTubeAPI youTubeAPI = new YouTubeAPI(site);
            TrelloAPI trelloAPI = new TrelloAPI();
            String idCard = trelloAPI.AddCard(youTubeAPI.parsingTitle(),site);
            String idCheckList = trelloAPI.AddCheckList(idCard,"Чек-лист");
                    for (String s: new YouTubeAPI(site).parsingContent()) {
            trelloAPI.AddCheckBox(idCheckList, s);
            //youTubeAPI.writeFile(youTubeAPI.parsingContent(), site);
        }

        } else if (site.matches(".*\\bcoursehunters\\b.*")) {
            CourseHunters courseHunters = new CourseHunters(site);
            TrelloAPI trelloAPI = new TrelloAPI();
            String idCard = trelloAPI.AddCard(courseHunters.parsingTitle(),site);
            String idCheckList = trelloAPI.AddCheckList(idCard,"Чек-лист");
            ArrayList<String> content = new CourseHunters(site).parsingContent();
            for (String s: content) {
                if (content.indexOf(s) == 200) {
                    idCheckList = trelloAPI.AddCheckList(idCard,"Чек-лист");
                }
                trelloAPI.AddCheckBox(idCheckList, s);
            }
            //courseHunters.writeFile(courseHunters.parsingContent(), site);
        } else {
            logger.error("Site not defined");
        }
    }
}