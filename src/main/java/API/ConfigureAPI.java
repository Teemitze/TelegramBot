package API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public interface ConfigureAPI {
    Logger logger = LoggerFactory.getLogger(ConfigureAPI.class);

    String TRELLO_KEY_API = loadProperty().getProperty("trello.key");
    String TRELLO_TOKEN = loadProperty().getProperty("trello.token");
    String TRELLO_ID_LIST = loadProperty().getProperty("trello.idList");

    String YOUTUBE_KEY_API = loadProperty().getProperty("youtube.key");


    static Properties loadProperty() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("./config.properties")) {
            property.load(fis);
            return property;
        } catch (IOException e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!");
        }
        return property;
    }
}
