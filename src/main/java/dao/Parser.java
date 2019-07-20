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

    default void writeFile(ArrayList<String> content, String site) {
        if (!content.isEmpty()) {
            try {
                FileWriter writer = new FileWriter("result.txt");
                writer.write(parsingTitle() + '\n' + '\n');
                int i = 1;

                writer.write(site + '\n' + '\n');
                for (String e : content) {
                    writer.write(i + ") " + e + '\n');
                    i++;
                }
                writer.flush();
            } catch (IOException e) {
                logger.error("Could not create/write file");
                e.printStackTrace();
            }
        } else {
            logger.error("The list of items to parse is empty");
        }
    }
}
