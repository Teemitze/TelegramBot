package configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public final String trelloApiKey;
    public final String trelloToken;
    public final String trelloIdList;
    public final String youtubeApiKey;
    public final String telegramBotName;
    public final String telegramToken;

    public Config(String trelloApiKey, String trelloToken, String trelloIdList,
                  String youtubeApiKey, String telegramBotName, String telegramToken) {

        this.trelloApiKey = trelloApiKey;
        this.trelloToken = trelloToken;
        this.trelloIdList = trelloIdList;
        this.youtubeApiKey = youtubeApiKey;
        this.telegramBotName = telegramBotName;
        this.telegramToken = telegramToken;
    }

    public static Config loadConfig() {
        try (final InputStream fis = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            final Properties property = new Properties();
            property.load(fis);
            final String trelloApiKey = property.getProperty("trello.key");
            final String trelloToken = property.getProperty("trello.token");
            final String trelloIdList = property.getProperty("trello.idList");
            final String youtubeApiKey = property.getProperty("youtube.key");
            final String telegramBotName = property.getProperty("telegram.botName");
            final String telegramToken = property.getProperty("telegram.token");
            return new Config(trelloApiKey, trelloToken, trelloIdList, youtubeApiKey, telegramBotName, telegramToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}