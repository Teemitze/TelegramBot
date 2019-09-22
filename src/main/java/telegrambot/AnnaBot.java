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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AnnaBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(AnnaBot.class);

    public AnnaBot() {
        try {
            Properties property = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            TOKEN = property.getProperty("telegram.token");
            BOT_NAME = property.getProperty("telegram.botName");
        } catch (Exception e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!");
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

    private static String BOT_NAME;
    private static String TOKEN;

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
                "Что приготовить?",
                "Добавить рецепт",
                "Удалить рецепт",
                "Показать все рецепты"
                /*, "Какая сейчас погода?",
                "Какая завтра погода?"*/);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = null;
            switch (update.getMessage().getText()) {
                case "/start":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("Данный бот может:\n\n" +
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
                                    "❌ Убраться в вашем доме"
                            );
                    setButtons(message);
                    break;
                case "Что приготовить?":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(new RecipeRepository().getOneRecipe().getNameRecipe());
                    break;
                case "Показать все рецепты":
                    String recipeName = "";

                    List<Recipe> recipes = new RecipeRepository().getAllRecipes();

                    for (Recipe recipe : recipes) {
                        recipeName += (recipe.getId()) + ") " + recipe.getNameRecipe() + "\n";
                    }
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(recipeName);
                    break;
                case "Добавить рецепт":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("Чтобы добавить рецепт напишите команду /add [Название рецепта]");
                    break;
                case "Удалить рецепт":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("Чтобы удалить рецепт напишите команду /del [ID рецепта]");
                    break;
                case "Какая сейчас погода?":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(new WeatherDays().nowWeather());
                    break;
                case "Какая завтра погода?":
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(new WeatherDays().tomorrowWeather());
                    break;
                default:
                    if (update.getMessage().getText().matches("/add(\\s)[а-яА-Я(\\s)]+")) {
                        new RecipeRepository().addRecipe(update.getMessage().getText().substring(5), new ArrayList<>());
                        message = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Блюдо " + update.getMessage().getText().substring(4) + " было добавлено!");
                    } else if (update.getMessage().getText().matches("/del(\\s)[0-9]")) {
                        new RecipeRepository().deleteRecipe(Integer.parseInt(update.getMessage().getText().substring(5)));
                        message = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Блюдо " + update.getMessage().getText().substring(5) + " было удалено!");
                    } else if (update.getMessage().getText().contains("http")) {
                        String site = update.getMessage().getText();
                        CheckSite checkSite = new CheckSite(site);
                        Parser object = checkSite.distributorSite();
                        String title = object.parsingTitle();

                        message = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Я добавила карточку " + "\"" + title + "\"" + " в ваш список Trello Bot!");

                        TrelloAPI trelloAPI = new TrelloAPI();
                        trelloAPI.trelloFillColumn(object);
                    } else if (update.hasCallbackQuery()) {
                        try {
                            String recipe = update.getCallbackQuery().getData();

                            RecipeRepository recipeRepository = new RecipeRepository();

                            int recipeId = recipeRepository.findRecipeName(recipe).getId();

                            recipeRepository.deleteRecipe(recipeId);

                            execute(new SendMessage().setText("Рецепт " +
                                    update.getCallbackQuery().getData() + " был удалён!")
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
            }
            try {
                execute(message);
                setButtons(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}