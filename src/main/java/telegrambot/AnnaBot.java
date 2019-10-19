package telegrambot;

import API.trello.TrelloAPI;
import API.yandexWeather.WeatherDays;
import dao.CheckSite;
import food.Recipe;
import food.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parsers.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AnnaBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(AnnaBot.class);

    public final String HELP = "/help";
    public final String WHAT_COOK = "Что приготовить?";
    public final String ADD_RECIPE = "Добавить рецепт";
    public final String DELETE_RECIPE = "Удалить рецепт";
    public final String SHOW_RECIPES = "Показать все рецепты";
    public final String WEATHER_NOW = "Какая сейчас погода?";
    public final String WEATHER_TOMORROW = "Какая завтра погода?";

    public final String INSTRUCTION_ADD_RECIPE = "Чтобы добавить рецепт напишите команду /add [Название рецепта]";
    public final String INSTRUCTION_DELETE_RECIPE = "Чтобы удалить рецепт напишите команду /del [ID рецепта]";

    private static String BOT_NAME;
    private static String TOKEN;

    public AnnaBot() {
        try (FileInputStream fis = new FileInputStream("./config.properties")) {
            Properties property = new Properties();
            property.load(fis);
            TOKEN = property.getProperty("telegram.token");
            BOT_NAME = property.getProperty("telegram.botName");
        } catch (IOException e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    private List<KeyboardRow> createButtons(String... buttons) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String buttonText : buttons) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(buttonText));
            keyboard.add(keyboardRow);
        }
        return keyboard;
    }

    private synchronized void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> keyboard = createButtons(
                WHAT_COOK,
                ADD_RECIPE,
                DELETE_RECIPE,
                SHOW_RECIPES
                /*, WEATHER_NOW,
                WEATHER_TOMORROW*/);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public String botInfo() {
        String info =
                "Данный бот может:\n\n" +
                        "✅ Показать случайный рецепт\n\n" +
                        "✅ Добавить рецепт\n\n" +
                        "✅ Удалить рецепт\n\n" +
                        "✅ Показать все рецепты\n\n" +
                        "✅ Получить все ролики из плейлиста YouTube и добавить их в ваш Trello\n\n" +
                        "✅ Получить все ролики из списка уроков coursehunter.net и добавить их в ваш Trello\n\n" +
                        "Данный бот не может:\n\n" +
                        "❌ Выгулять вашего домашнего питомца\n\n" +
                        "❌ Помыть посуду\n\n" +
                        "❌ Сходить в магазин\n\n" +
                        "❌ Убраться в вашем доме";
        return info;
    }

    public SendMessage newSendMessage(Update update, String message) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(message);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message;

            switch (update.getMessage().getText()) {
                case HELP -> message = newSendMessage(update, botInfo());
                case WHAT_COOK -> message = newSendMessage(update, new RecipeRepository().getOneRecipe().getNameRecipe());
                case SHOW_RECIPES -> message = newSendMessage(update, allRecipes());
                case ADD_RECIPE -> message = newSendMessage(update, INSTRUCTION_ADD_RECIPE);
                case DELETE_RECIPE -> message = newSendMessage(update, INSTRUCTION_DELETE_RECIPE);
                case WEATHER_NOW -> message = newSendMessage(update, new WeatherDays().nowWeather());
                case WEATHER_TOMORROW -> message = newSendMessage(update, new WeatherDays().tomorrowWeather());
                default -> message = method(update);
            }
            try {
                execute(message);
                setButtons(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String allRecipes() {
        List<Recipe> recipes = new RecipeRepository().getAllRecipes();

        String recipeName = "";
        for (Recipe recipe : recipes) {
            recipeName += (recipe.getId()) + ") " + recipe.getNameRecipe() + "\n";
        }
        return recipeName;
    }


    public SendMessage method(Update update) {
        final String RECIPE = "Рецепт ";

        SendMessage message = null;

        if (update.getMessage().getText().matches("/add(\\s)[а-яА-Я(\\s)]+")) {
            new RecipeRepository().addRecipe(update.getMessage().getText().substring(5), new ArrayList<>());
            message = newSendMessage(update, RECIPE + update.getMessage().getText().substring(5) + " было добавлено!");
        } else if (update.getMessage().getText().matches("/del(\\s)\\d+$")) {
            new RecipeRepository().deleteRecipe(Integer.parseInt(update.getMessage().getText().substring(5)));
            message = newSendMessage(update, RECIPE + update.getMessage().getText().substring(5) + " было удалено!");
        } else if (update.getMessage().getText().contains("http")) {
            String site = update.getMessage().getText();
            CheckSite checkSite = new CheckSite(site);
            Parser object = checkSite.distributorSite();
            String title = object.parsingTitle();

            message = newSendMessage(update, "Я добавила карточку " + "\"" + title + "\"" + " в ваш список Trello Bot!");

            TrelloAPI trelloAPI = new TrelloAPI();
            trelloAPI.trelloFillColumn(object);
        } else if (update.hasCallbackQuery()) {
            try {
                String recipeRequest = update.getCallbackQuery().getData();
                RecipeRepository recipeRepository = new RecipeRepository();
                int recipeId = recipeRepository.findRecipeName(recipeRequest).getId();
                recipeRepository.deleteRecipe(recipeId);
                execute(newSendMessage(update, RECIPE + update.getCallbackQuery().getData() + " был удалён!").setChatId(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                logger.error("TelegramApiException!", e);
                throw new RuntimeException(e);
            }
        }
        return message;
    }
}