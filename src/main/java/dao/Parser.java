package dao;

import org.jsoup.select.Elements;

public interface Parser {
    String parsingTitle();
    Elements parsingContent();
    void writeFile(Elements elements);
}
