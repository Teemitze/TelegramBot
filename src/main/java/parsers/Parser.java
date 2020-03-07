package parsers;

import java.util.List;

public interface Parser {
    String parsingTitle(String content);

    List<String> parsingContent();

    String getSite();
}
