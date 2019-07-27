package telegrambot;

import dao.CheckSite;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TrelloBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());

            System.out.println(update.getMessage().getText());
            CheckSite checkSite = new CheckSite(update.getMessage().getText());
            checkSite.validateSite();
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "my_trello_teemitze_bot";
    }

    @Override
    public String getBotToken() {
        return "807526734:AAHs1NG1qQz3PE3hl5zZZnAtF08q-mhl3f8";
    }
}