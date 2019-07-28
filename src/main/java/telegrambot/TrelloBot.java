package telegrambot;

import dao.CheckSite;
import dao.Parser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import trello.TrelloAPI;

public class TrelloBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String site = update.getMessage().getText();
            CheckSite checkSite = new CheckSite(site);
            Parser object = checkSite.distributorSite();
            String title = object.parsingTitle();


            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Я добавила карточку " +  "\"" + title + "\"" + " в ваш список Trello Bot!");


            TrelloAPI trelloAPI = new TrelloAPI();
            trelloAPI.trelloFillCard(object);

            try {
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