package telegrambot;

import API.trello.TrelloHelper;
import API.trello.dto.TrelloCard;
import configuration.Config;
import dataBase.ConnectionFactory;
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
import parsers.Parser;
import parsers.api.youtube.YouTubeParser;
import parsers.html.CourseHuntersParser;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    List<Recipe> recipes = new ArrayList<>();

    private final Config config = Config.loadConfig();
    private final String BOT_NAME = config.telegramBotName;
    private final String TOKEN = config.telegramToken;
    private final String url = "jdbc:postgresql://localhost:5432/telegram_bot";
    private final String userDB = "postgres";
    private final String passwordDB = "postgres";
    public final String INSTRUCTION_ADD_RECIPE = "Чтобы добавить рецепт напишите команду /add [Название рецепта]";
    public final String INSTRUCTION_DELETE_RECIPE = "Чтобы удалить рецепт напишите команду /del [ID рецепта]";

    final RecipeRepository recipeRepository = new RecipeRepository(ConnectionFactory.getConnection(url, userDB, passwordDB));

    final String infoBot = """
            Данный бот может:

            ✅ Показать случайный рецепт

            ✅ Добавить рецепт

            ✅ Удалить рецепт

            ✅ Показать все рецепты

            ✅ Получить все ролики из плейлиста YouTube и добавить их в ваш Trello

            ✅ Получить все ролики из списка уроков coursehunter.net и добавить их в ваш Trello

            Данный бот не может:

            ❌ Выгулять вашего домашнего питомца

            ❌ Помыть посуду

            ❌ Сходить в магазин

            ❌ Убраться в вашем доме
                     """;

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
                Menu.ADD_RECIPE,
                Menu.DELETE_RECIPE,
                Menu.SHOW_RECIPES);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public SendMessage newSendMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId()).setText(message);
        return sendMessage;
    }

    public SendMessage startAndHelp(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId()).setText(infoBot);
        setButtons(sendMessage);
        return sendMessage;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            Recipe recipe = recipeRepository.findRecipeName(update.getCallbackQuery().getMessage().getText());
            recipes.add(recipe);
            return;
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message;
            switch (update.getMessage().getText()) {
                case Menu.HELP, Menu.START -> message = startAndHelp(update);
                case Menu.SHOW_RECIPES -> message = newSendMessage(update, getAllRecipes());
                case Menu.ADD_RECIPE -> message = newSendMessage(update, INSTRUCTION_ADD_RECIPE);
                case Menu.DELETE_RECIPE -> message = newSendMessage(update, INSTRUCTION_DELETE_RECIPE);
                default -> message = method(update);
            }
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAllRecipes() {
        Set<Recipe> recipes = new TreeSet<>(recipeRepository.getAllRecipes());

        if (recipes.isEmpty()) {
            return "Рецептов в базе не найдено.";
        }

        StringBuilder recipeName = new StringBuilder();
        for (Recipe recipe : recipes) {
            recipeName.append(recipe.getId()).append(") ").append(recipe.getNameRecipe()).append("\n");
        }
        return recipeName.toString();
    }

    public String getIngredientsById(int id) {
        Recipe recipe = recipeRepository.findRecipeByID(id);
        if (recipe == null) return "Такого рецепта нет!";
        StringBuilder stringBuilder = new StringBuilder("Рецепт " + recipe.getNameRecipe() + " состоит из:\n");

        final String orangeDiamond = "\uD83D\uDD38";

        for (String ingredient : recipe.getIngredients()) {
            stringBuilder.append(orangeDiamond).append(ingredient).append("\n");
        }
        return stringBuilder.toString();
    }

    public SendMessage method(Update update) {
        final String RECIPE = "Рецепт ";
        String userMessage = update.getMessage().getText();
        SendMessage message = null;
        String result = "";
        if (userMessage.matches("/add(\\s)[а-яА-Я(\\s)]+")) {
            Recipe recipe = new Recipe();
            List<String> ingredients = new ArrayList<>();
            if (userMessage.contains("\n")) {
                int newLine = userMessage.indexOf("\n");
                recipe.setNameRecipe(userMessage.substring(5, newLine));
                userMessage.lines().skip(1).forEach(ingredients::add);
                recipe.setIngredients(ingredients);
                result = RECIPE + userMessage.substring(5, newLine) + " был добавлен!";
            } else {
                recipe.setNameRecipe(userMessage.substring(5));
                recipe.setIngredients(ingredients);
                result = RECIPE + userMessage.substring(5) + " был добавлен!";
            }
            recipeRepository.addRecipe(recipe);
            message = newSendMessage(update, result);
        } else if (userMessage.matches("/del(\\s)\\d+$")) {
            Recipe recipe = recipeRepository.findRecipeByID(Integer.parseInt(userMessage.substring(5)));
            if (recipe != null) {
                message = newSendMessage(update, RECIPE + recipe.getNameRecipe() + " был удалён!");
                recipeRepository.deleteRecipe(Integer.parseInt(userMessage.substring(5)));
            } else {
                message = newSendMessage(update, "Такого рецепта нет в базе!");
            }
        } else if (userMessage.matches("[0-9]+")) {
            String recipe = getIngredientsById(Integer.parseInt(userMessage));
            message = newSendMessage(update, recipe);
        } else if (isSiteCanBeParsed(userMessage)) {
            Parser parser = distributorSite(userMessage).get();
            TrelloCard trelloCard = TrelloHelper.trelloFillCard(parser);
            return newSendMessage(update, "Я добавила карточку " + "\"" + trelloCard.getName() + "\"" + " в ваш список Trello Bot!");
        }
        return message;
    }


    public Optional<Parser> distributorSite(String site) {

        Optional<Parser> result = Optional.empty();

        if (site.matches("^https?+://(www\\.)?youtube.com/playlist\\?list=.{18,34}$")) {
            result = Optional.of(new YouTubeParser(site));
        } else if (site.matches("^https?+://(www\\.)?coursehunter.net/course/.*$")) {
            result = Optional.of(new CourseHuntersParser(site));
        } else {
            logger.error("Site not defined");
        }

        return result;
    }

    private boolean isSiteCanBeParsed(String site) {
        return distributorSite(site).isPresent();
    }
}