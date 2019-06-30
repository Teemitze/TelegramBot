package youtube;

import dao.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class YouTube implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(YouTube.class);

    private String site;
    private Document doc;

    public YouTube(String site) {
        try {
            this.site = site;
            this.doc = Jsoup.connect(site).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String parsingTitle() {
        return doc.getElementsByTag("h1").toggleClass("yt-simple-endpoint style-scope yt-formatted-string").text();
    }

    @Override
    public Elements parsingContent() {
        return doc.getElementsByTag("tr");
    }

    @Override
    public void writeFile(Elements elements) {
        if (elements != null) {
            try {
                FileWriter writer = new FileWriter("youtube.txt", false);
                writer.write(parsingTitle() + '\n' + '\n');
                int i = 1;

                writer.write(site + '\n' + '\n');
                for (Element e : elements) {
                    writer.write(i + ") " + e.attr("data-title") + '\n');
                    i++;
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("Item list is empty!");
        }
    }
}
