package API.yandexWeather;

import java.util.HashMap;

class YandexFillParameters {
    HashMap<String, String> fillParametersForWeather() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("lat", "55.604171");
        parameters.put("lon", "37.665723");
        parameters.put("lang", "ru_RU");
        parameters.put("limit", "1");
        parameters.put("hours", "false");
        parameters.put("extra", "false");
        return parameters;
    }
}
