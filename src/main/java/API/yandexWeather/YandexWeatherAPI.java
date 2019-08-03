package API;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class YandexWeatherAPI {

    private final Logger logger = LoggerFactory.getLogger(YandexWeatherAPI.class);

    private JSONObject getWeather() {
        HttpClient client = new HttpClient();

        URIBuilder builder = null;

        String YandexWeatherAPI = "https://api.weather.yandex.ru/v1/forecast?";
        try {
            builder = new URIBuilder(YandexWeatherAPI);
            builder.addParameter("lat", "55.604171");
            builder.addParameter("lon", "37.665723");
            builder.addParameter("lang", "ru_RU");
            builder.addParameter("limit", "1");
            builder.addParameter("hours", "false");
            builder.addParameter("extra", "false");
        } catch (URISyntaxException e) {
            logger.error("Could not get the result using YouTube API");
            e.printStackTrace();
        }

        GetMethod method = new GetMethod(builder.toString());
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

    private String nowWeather(JSONObject jsonObject){
        String time = jsonObject.getString("now_dt"); // Время сервера в UTC.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime date = LocalDateTime.parse(time, dateTimeFormatter).plusHours(3L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, EEEE dd MMMM");
        String formatDateTime = date.format(formatter);

        // Погода в данный момент
        int temp = jsonObject.getJSONObject("fact").getInt("temp"); // Температура (°C).
        int feels_like = jsonObject.getJSONObject("fact").getInt("feels_like"); // Ощущаемая температура (°C).
//        String icon = jsonObject.getJSONObject("fact").getString("icon"); // Код иконки погоды. Иконка доступна по адресу https://yastatic.net/weather/i/icons/blueye/color/svg/<значение из поля icon>.svg.
        String condition = jsonObject.getJSONObject("fact").getString("condition"); // Код расшифровки погодного описания. Возможные значения:
        int wind_speed = jsonObject.getJSONObject("fact").getInt("wind_speed"); // Скорость ветра (в м/с).
        int humidity = jsonObject.getJSONObject("fact").getInt("humidity"); // Влажность воздуха (в процентах).


        condition = translateWeatherCondition(condition);

        String result = "Сейчас " + formatDateTime + ". На улице: " + condition + ". Температура: " + temp + "°C. " + "Ощущается как: " + feels_like + "°C." + " Скорость ветра " + wind_speed + " м/с" +
                " Влажность воздуха: " + humidity +  "% ";

        return result;
    }

    private String tomorrowWeather(JSONObject jsonObject) {
        JSONObject tomorrow = jsonObject.getJSONArray("forecasts").getJSONObject(1);
        String date = tomorrow.getString("date");

        String result = "Завтра " + date + '\n' + partsDay(jsonObject, "morning") + '\n' + partsDay(jsonObject, "day") + '\n' + partsDay(jsonObject, "evening");


        return result;
    }

    private String translateWeatherCondition(String condition){
        switch (condition) {
            case ("clear"):
                condition = "ясно";
                break;
            case ("partly-cloudy"):
                condition = "малооблачно";
                break;
            case ("cloudy"):
                condition = "облачно с прояснениями";
                break;
            case ("overcast"):
                condition = "пасмурно";
                break;
            case ("partly-cloudy-and-light-rain"):
                condition = "небольшой дождь";
                break;
            case ("partly-cloudy-and-rain"):
                condition = "дождь";
                break;
            case ("overcast-and-rain"):
                condition = "сильный дождь";
                break;
            case ("overcast-thunderstorms-with-rain"):
                condition = "сильный дождь, гроза.";
                break;
            case ("cloudy-and-light-rain"):
                condition = "небольшой дождь";
                break;
            case ("overcast-and-light-rain"):
                condition = "небольшой дождь";
                break;
            case ("cloudy-and-rain"):
                condition = "дождь";
                break;
            case ("overcast-and-wet-snow"):
                condition = "дождь со снегом";
                break;
            case ("partly-cloudy-and-light-snow"):
                condition = "небольшой снег";
                break;
            case ("partly-cloudy-and-snow"):
                condition = "снег";
                break;
            case ("overcast-and-snow"):
                condition = "снегопад";
                break;
            case ("cloudy-and-light-snow"):
                condition = "небольшой снег";
                break;
            case ("overcast-and-light-snow"):
                condition = "небольшой снег";
                break;
            case ("cloudy-and-snow"):
                condition = "снег";
                break;
        }

        return condition;
    }

    private String translatePartsDay(String part){
        switch (part) {
            case "morning":
                part = "Утром";
                break;
            case "day":
                part = "Днём";
                break;
            case "evening":
                part = "Вечером";
                break;
        }
        return part;
    }


    private String partsDay(JSONObject jsonObject, String part){
        JSONObject tomorrow = jsonObject.getJSONArray("forecasts").getJSONObject(1);

        JSONObject morning = tomorrow.getJSONObject("parts").getJSONObject(part);
        int morningTemp = morning.getInt("temp_avg"); // Температура
        int morningTempLike = morning.getInt("feels_like"); // Ощущается
       // String icon = morning.getString("icon");
        String condition = morning.getString("condition"); // Код расшифровки погодного описания. Возможные значения:
        int wind_speed = morning.getInt("wind_speed"); // Скорость ветра (в м/с).
        int humidity = morning.getInt("humidity"); // Влажность воздуха (в процентах).

        condition = translateWeatherCondition(condition);
        part = translatePartsDay(part);

        String result = part + " " + condition + ". Температура: " + morningTemp + "°C. " + "Ощущается как: " + morningTempLike + "°C." + " Скорость ветра " + wind_speed + " м/с" +
                " Влажность воздуха: " + humidity +  "% ";

        return result;
    }



    public static void main(String[] args) {
        YandexWeatherAPI yandexWeatherAPI = new YandexWeatherAPI();
        JSONObject jsonObject =  yandexWeatherAPI.getWeather();
        System.out.println(yandexWeatherAPI.tomorrowWeather(jsonObject));
    }
}
