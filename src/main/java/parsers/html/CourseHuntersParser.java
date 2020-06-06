package parsers.html;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parsers.Parser;

import java.util.List;
import java.util.Optional;

import static parsers.html.HTMLParserUtil.getSiteDOM;

public class CourseHuntersParser implements Parser {

    private final String URL;

    public CourseHuntersParser(String URL) {
        this.URL = URL;
    }

    @Override
    public Optional<String> getTitle() {
        Optional<Document> dom = getSiteDOM(URL);
        if (dom.isPresent()) {
            return dom.map(doc -> doc.getElementsByClass("hero-title")).map(Elements::text);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public Optional<List<String>> getContent() {
        Optional<Document> dom = getSiteDOM(URL);
        if (dom.isPresent()) {
            return dom.map(d -> d.getElementsByClass("lessons-name"))
                    .map(Elements::eachText);
        } else {
            return Optional.empty();
        }
    }
}
