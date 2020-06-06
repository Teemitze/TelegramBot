package parsers;

import java.util.List;
import java.util.Optional;

public interface Parser {

    Optional<String> getTitle();

    String getURL();

    Optional<List<String>> getContent();
}
