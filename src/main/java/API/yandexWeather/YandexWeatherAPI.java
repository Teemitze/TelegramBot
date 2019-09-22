package API.yandexWeather;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static API.HttpBuilder.yandexWeatherBuilder;

public class YandexWeatherAPI {
    private String KEY_API;

    YandexWeatherAPI() {
        try {
            Properties property = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            KEY_API = property.getProperty("yandexWeather.key");
        } catch (Exception e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    private final Logger logger = LoggerFactory.getLogger(YandexWeatherAPI.class);

    public final static String YANDEX_WEATHER_API = "https://api.weather.yandex.ru/v1/forecast?";

    JSONObject httpResponseWeather() {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(yandexWeatherBuilder(new YandexFillParameters().fillParametersForWeather()));
        method.addRequestHeader("X-Yandex-API-Key", KEY_API);
        JSONObject jsonObject = null;
        try {
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            jsonObject = new JSONObject(new String(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("httpResponseWeather IOException");
        }

        return jsonObject;
    }
}
