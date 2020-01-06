package parsers;

import java.util.List;

public interface Parser {
    String parsingTitle();

    List<String> parsingContent();

    String getSite();
}
