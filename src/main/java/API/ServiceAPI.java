package API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public interface ServiceAPI {
    Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

    default Properties loadProperty() {
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
