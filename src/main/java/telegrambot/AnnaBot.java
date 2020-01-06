package telegrambot;

import API.trello.TrelloHelper;
import food.Ingredient;
import food.Recipe;
import food.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import parsers.CheckSite;
import parsers.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class AnnaBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(AnnaBot.class);

    List<Recipe> recipes = new ArrayList<>();

    public final String HELP = "/help";
    public final String START = "/start";
    public final String WHAT_COOK = "Что приготовить?";
    public final String ADD_RECIPE = "Добавить рецепт";
    public final String DELETE_RECIPE = "Удалить рецепт";
    public final String SHOW_RECIPES = "Показать все рецепты";
    public final String SHOW_INGREDIENTS = "Показать все ингредиенты";

    public final String INSTRUCTION_ADD_RECIPE = "Чтобы добавить рецепт напишите команду /add [Название рецепта]";
    public final String INSTRUCTION_DELETE_RECIPE = "Чтобы удалить рецепт напишите команду /del [ID рецепта]";

    final String infoBot =
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
                SHOW_RECIPES,
                SHOW_INGREDIENTS);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    private InlineKeyboardMarkup setInline() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Добавить").setCallbackData("1"));
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
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

    public SendMessage getOneRecipe(Update update) {
        String recipeName = new RecipeRepository().getOneRecipe().getNameRecipe();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId()).setText(recipeName);
        sendMessage.setReplyMarkup(setInline());
        return sendMessage;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            RecipeRepository recipeRepository = new RecipeRepository();
            Recipe recipe = recipeRepository.findRecipeName(update.getCallbackQuery().getMessage().getText());
            recipes.add(recipe);
            return;
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message;
            switch (update.getMessage().getText()) {
                case HELP, START -> message = startAndHelp(update);
                case WHAT_COOK -> message = getOneRecipe(update);
                case SHOW_RECIPES -> message = newSendMessage(update, allRecipes());
                case ADD_RECIPE -> message = newSendMessage(update, INSTRUCTION_ADD_RECIPE);
                case DELETE_RECIPE -> message = newSendMessage(update, INSTRUCTION_DELETE_RECIPE);
                case SHOW_INGREDIENTS -> message = newSendMessage(update, getIngridients());
                default -> message = method(update);
            }
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String allRecipes() {
        Set<Recipe> recipes = new TreeSet<>(new RecipeRepository().getAllRecipes());

        String recipeName = "";
        for (Recipe recipe : recipes) {
            recipeName += (recipe.getId()) + ") " + recipe.getNameRecipe() + "\n";
        }
        return recipeName;
    }

    public String getIngridients() {
        String result = "";
        if (recipes.isEmpty()) {
            RecipeRepository recipeRepository = new RecipeRepository();
            recipes = recipeRepository.getAllRecipes();
        }
        for (Recipe recipe : recipes) {
            result += recipes.indexOf(recipe) + 1 + ") " + recipe.getNameRecipe() + ":\n";
            for (Ingredient ingredient : recipe.getIngredients()) {
                result += "\uD83D\uDD38" + ingredient.getNameIngredient() + "\n";
            }
            result += "\n";
        }
        recipes.clear();

        return result;
    }

    public String getIngridientsById(int id) {
        RecipeRepository recipeRepository = new RecipeRepository();
        Recipe recipe = recipeRepository.findRecipeByID(id);
        String result = "Рецепт " + recipe.getNameRecipe() + " состоит из:\n";
        for (Ingredient ingredient : recipe.getIngredients()) {
            result += "\uD83D\uDD38" + ingredient.getNameIngredient() + "\n";
        }
        return result;
    }

    public SendMessage method(Update update) {
        final String RECIPE = "Рецепт ";
        String userMessage = update.getMessage().getText();
        SendMessage message = null;
        String result = "";
        if (userMessage.matches("/add(\\s)[а-яА-Я(\\s)]+")) {
            Recipe recipe = new Recipe();
            if (userMessage.contains("\n")) {
                int newLine = userMessage.indexOf("\n");
                List<Ingredient> ingredients = new ArrayList<>();
                recipe.setNameRecipe(userMessage.substring(5, newLine));
                userMessage.lines().skip(1).map(um -> new Ingredient(um, recipe)).forEach(ingredients::add);
                recipe.setIngredients(ingredients);
                result = RECIPE + userMessage.substring(5, newLine) + " был добавлен!";
            } else {
                recipe.setNameRecipe(userMessage.substring(5));
                result = RECIPE + userMessage.substring(5) + " был добавлен!";
            }
            new RecipeRepository().addRecipe(recipe);
            message = newSendMessage(update, result);
        } else if (userMessage.matches("/del(\\s)\\d+$")) {
            RecipeRepository recipeRepository = new RecipeRepository();
            Recipe recipe = recipeRepository.findRecipeByID(Integer.parseInt(userMessage.substring(5)));
            if (recipe != null) {
                message = newSendMessage(update, RECIPE + recipe.getNameRecipe() + " был удалён!");
                recipeRepository.deleteRecipe(Integer.parseInt(userMessage.substring(5)));
            } else {
                message = newSendMessage(update, "Такого рецепта нет в базе!");
            }
        } else if (userMessage.matches("/rec(\\s)\\d+$")) {
            message = newSendMessage(update, getIngridientsById(Integer.parseInt(userMessage.substring(5))));
        } else if (userMessage.contains("http")) {
            String site = userMessage;
            CheckSite checkSite = new CheckSite(site);
            Parser object = checkSite.distributorSite();
            String title = object.parsingTitle();

            message = newSendMessage(update, "Я добавила карточку " + "\"" + title + "\"" + " в ваш список Trello Bot!");

            TrelloHelper trelloHelper = new TrelloHelper();
            trelloHelper.trelloFillCard(object);
        }
        return message;
    }
}