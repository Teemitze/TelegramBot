package API.yandexWeather;

import org.json.JSONObject;

public class WeatherDays {
    private JSONObject allWeather = new YandexWeatherAPI().httpResponseWeather();

    /**
     * Метод парсит allWeather, чтобы взять данные текущей погоды
     *
     * @return текущая погода
     */
    public String nowWeather() {
        String today = new Converter().converterDate(allWeather.getString("now_dt")); // Сегодняшняя дата
        int temperature = allWeather.getJSONObject("fact").getInt("temp"); // Температура в (°C)
        String condition = new Converter().translateWeatherCondition(allWeather.getJSONObject("fact").getString("condition")); // Погодное описание
        int wind_speed = allWeather.getJSONObject("fact").getInt("wind_speed"); // Скорость ветра (в м/с)
        int humidity = allWeather.getJSONObject("fact").getInt("humidity"); // Влажность воздуха (в %)

        StringBuilder result = new StringBuilder();
        result.append("Сейчас " + today + "\n")
                .append("На улице: " + condition + "\n")
                .append("Температура: " + temperature + "°C \n")
                .append("Ощущается как: " + temperature + "°C \n")
                .append("Скорость ветра: " + wind_speed + " м/с \n")
                .append("Влажность воздуха: " + humidity + "%");

        return result.toString();
    }

    /**
     * Метод возвращает погоду на завтра из 3 частей суток
     *
     * @return погода на завтра с заполненными данными
     */
    public String tomorrowWeather() {
        JSONObject tomorrow = allWeather.getJSONArray("forecasts").getJSONObject(1);
        String date = tomorrow.getString("date");
        String result = "Завтра " + date + '\n' + partsDay("morning") + '\n' + partsDay("day") + '\n' + partsDay("evening");

        return result;
    }

    /**
     * Метод парсит из ответа HTTP данные для определённых суток
     *
     * @param part часть суток (ночь, утро, день, вечер)
     * @return часть суток с заполненными значениями
     */
    private String partsDay(String part) {
        JSONObject tomorrow = allWeather.getJSONArray("forecasts").getJSONObject(1);

        JSONObject morning = tomorrow.getJSONObject("parts").getJSONObject(part);
        int morningTemp = morning.getInt("temp_avg"); // Температура
        int morningTempLike = morning.getInt("feels_like"); // Ощущается
        String condition = morning.getString("condition"); // Код расшифровки погодного описания. Возможные значения:
        int wind_speed = morning.getInt("wind_speed"); // Скорость ветра (в м/с).
        int humidity = morning.getInt("humidity"); // Влажность воздуха (в процентах).

        condition = new Converter().translateWeatherCondition(condition);
        part = new Converter().translatePartsDay(part);

        StringBuilder result = new StringBuilder()
                .append(part + " " + condition + "\n")
                .append("Температура: " + morningTemp + "°C \n")
                .append("Ощущается как: " + morningTempLike + "°C \n")
                .append("Скорость ветра " + wind_speed + " м/с \n")
                .append("Влажность воздуха: " + humidity + "% \n");

        return result.toString();
    }
}