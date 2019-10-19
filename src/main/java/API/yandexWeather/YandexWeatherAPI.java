package API.yandexWeather;

import API.ServiceAPI;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static API.HttpBuilder.yandexWeatherBuilder;

public class YandexWeatherAPI implements ServiceAPI {
    private String KEY_API;

    YandexWeatherAPI() {
        KEY_API = loadProperty().getProperty("yandexWeather.key");
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
