package telegrambot;

import API.yandexWeather.WeatherDays;
import dao.CheckSite;
import parsers.Parser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import API.trello.TrelloAPI;

import java.util.ArrayList;
import java.util.List;

public class AnnaBot extends TelegramLongPollingBot {

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow cookButton = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        cookButton.add(new KeyboardButton("Что приготовить?"));

        KeyboardRow weatherNowButton = new KeyboardRow();
        weatherNowButton.add(new KeyboardButton("Какая сейчас погода?"));

        KeyboardRow weatherTomorrowButton = new KeyboardRow();
        weatherTomorrowButton.add(new KeyboardButton("Какая завтра погода?"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(cookButton);
        keyboard.add(weatherNowButton);
        keyboard.add(weatherTomorrowButton);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            SendMessage message;
            if (update.getMessage().getText().equals("Что приготовить?")) {
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(new Menu().getOneFood());
            } else if (update.getMessage().getText().equals("Какая сейчас погода?")) {
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(new WeatherDays().nowWeather());
            } else if (update.getMessage().getText().equals("Какая завтра погода?")) {
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(new WeatherDays().tomorrowWeather());
            } else {
                String site = update.getMessage().getText();
                CheckSite checkSite = new CheckSite(site);
                Parser object = checkSite.distributorSite();
                String title = object.parsingTitle();

                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText("Я добавила карточку " + "\"" + title + "\"" + " в ваш список Trello Bot!");

                TrelloAPI trelloAPI = new TrelloAPI();
                trelloAPI.trelloFillCard(object);
            }

            try {
                setButtons(message);
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "teemitze_anna_bot";
    }

    @Override
    public String getBotToken() {
        return "980903793:AAEx6PGHJF1jcvDQW5t6DGCw7Q7OQavT_NE";
    }
}