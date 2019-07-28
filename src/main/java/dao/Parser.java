package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public interface Parser {
    Logger logger = LoggerFactory.getLogger(Parser.class);

    String parsingTitle();

    ArrayList parsingContent();

    String getSite();

    default void writeFile(ArrayList<String> content, String site) {
        if (!content.isEmpty()) {
            try {
                FileWriter writer = new FileWriter("result.txt");
                String title = parsingTitle();
                if (title != null) {
                    writer.write(title + '\n' + '\n');
                    logger.info("Title: {}", title);
                    writer.write(site + '\n' + '\n');
                    for (String element : content) {
                        writer.write((content.indexOf(element) + 1) + ") " + element + '\n');
                    }
                } else {
                    logger.error("Title is empty!");
                }
                writer.flush();
            } catch (IOException e) {
                logger.error("Input output error!");
            }
        } else {
            logger.error("Content is empty!");
        }
    }
}
