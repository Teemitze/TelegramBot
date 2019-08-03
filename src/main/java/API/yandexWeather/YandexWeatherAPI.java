package API.yandexWeather;

import API.API;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class YandexWeatherAPI implements API {

    private final Logger logger = LoggerFactory.getLogger(YandexWeatherAPI.class);

     JSONObject httpResponseWeather() {
        HttpClient client = new HttpClient();

        String YandexWeatherAPI = "https://api.weather.yandex.ru/v1/forecast?";
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("lat", "55.604171");
        parameters.put("lon", "37.665723");
        parameters.put("lang", "ru_RU");
        parameters.put("limit", "1");
        parameters.put("hours", "false");
        parameters.put("extra", "false");

        GetMethod method = new GetMethod(addParameters(YandexWeatherAPI, parameters));
        method.addRequestHeader("X-Yandex-API-Key", "ba77baf3-e8ed-424b-a6bb-b399f1067318");
        JSONObject jsonObject = null;
        try {
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
             jsonObject = new JSONObject(new String(responseBody));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
