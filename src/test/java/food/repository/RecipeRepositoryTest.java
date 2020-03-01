package food.repository;

import dataBase.ConnectionFactory;
import food.Recipe;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class RecipeRepositoryTest {

    private final String url = "jdbc:postgresql://localhost:5432/telegram_bot_test";
    private final String userDB = "postgres";
    private final String passwordDB = "postgres";

    Connection connection = ConnectionFactory.getConnection(url, userDB, passwordDB);
    RecipeRepository recipeRepository = new RecipeRepository(connection);

    public Recipe createRecipe(String nameRecipe, String... ingredients) {
        return new Recipe(nameRecipe, Arrays.asList(ingredients));
    }

    @Test
    public void testAddRecipe() {
        Recipe recipe = createRecipe("Суп", "Мясо", "Вода");
        recipeRepository.addRecipe(recipe);
        Recipe recipeGetBase = recipeRepository.findRecipeName("Суп");
        Assert.assertEquals(recipeGetBase.getIngredients(), recipe.getIngredients());
    }

    @Test
    public void testDeleteRecipe() {
        Recipe recipeToBase = createRecipe("Шаурма", "Котёнок", "Лаваш");
        recipeRepository.addRecipe(recipeToBase);
        Recipe recipeGetBase = recipeRepository.findRecipeName("Шаурма");

        recipeRepository.deleteRecipe(recipeGetBase.getId());
        Assert.assertEquals(recipeRepository.findRecipeByID(recipeGetBase.getId()), null);
    }

    @Test
    public void testGetAllRecipes() {
        Recipe recipe1 = createRecipe("Каша Геркулес", "Овсянка", "Геркулес");
        Recipe recipe2 = createRecipe("Бутерброд ", "Колбаса", "Сыр");
        Recipe recipe3 = createRecipe("Гренки", "Чеснок", "Хлеб");

        recipeRepository.addRecipe(recipe1);
        recipeRepository.addRecipe(recipe2);
        recipeRepository.addRecipe(recipe3);

        Assert.assertEquals(recipeRepository.getAllRecipes().size(), 3);
    }

    @Test
    public void testFindRecipeName() {
        Recipe recipe = createRecipe("Завтрак туриста", "Белка", "Ворубушек");
        recipeRepository.addRecipe(recipe);
        Assert.assertEquals(recipeRepository.findRecipeName("Завтрак туриста").getNameRecipe(), "Завтрак туриста");
    }

    @Test
    public void testFindRecipeByID() {
        Recipe recipeToBase = createRecipe("Хлопья", "Молоко", "Хлопья");
        recipeRepository.addRecipe(recipeToBase);
        Recipe recipeGetBase = recipeRepository.findRecipeName("Хлопья");
        Assert.assertEquals(recipeGetBase.getNameRecipe(), recipeToBase.getNameRecipe());
    }

    @AfterMethod
    public void deleteAllRecipes() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM recipes WHERE id = id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE recipes(id SERIAL PRIMARY KEY, name VARCHAR NOT NULL UNIQUE)");
            statement.execute("CREATE TABLE ingredients(id SERIAL PRIMARY KEY, name VARCHAR NOT NULL, recipe_id INT, CONSTRAINT recipe_id_fk FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void dropTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE ingredients");
            statement.execute("DROP TABLE recipes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}