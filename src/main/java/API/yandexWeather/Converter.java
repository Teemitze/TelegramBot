package API.yandexWeather;

 class Translate {

     /**
      * Метод переводит значение condition из HTTP ответа на русский
      * @param condition погодное описание на английском
      * @return погодное описание на русском
      */
     String translateWeatherCondition(String condition){
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

     /**
      * Метод переводит одно из значений parts из HTTP ответа на русский
      * @param part часть суток на английском
      * @return часть суток на русском
      */
     String translatePartsDay(String part){
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
}
