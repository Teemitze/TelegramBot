package configuration;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {

    public final String trelloApiKey;
    public final String trelloToken;
    public final String trelloIdList;
    public final String youtubeApiKey;

    public Config(String trelloApiKey, String trelloToken, String trelloIdList, String youtubeApiKey) {
        this.trelloApiKey = trelloApiKey;
        this.trelloToken = trelloToken;
        this.trelloIdList = trelloIdList;
        this.youtubeApiKey = youtubeApiKey;
    }

    public static Config loadConfig() {
        try (final FileInputStream fis = new FileInputStream("./config.properties")) {
            final Properties property = new Properties();
            property.load(fis);
            final String trelloApiKey = property.getProperty("trello.key");
            final String trelloToken = property.getProperty("trello.token");
            final String trelloIdList = property.getProperty("trello.idList");
            final String youtubeApiKey = property.getProperty("youtube.key");
            return new Config(trelloApiKey, trelloToken, trelloIdList, youtubeApiKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}