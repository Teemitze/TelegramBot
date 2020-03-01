package food.repository;

import food.Recipe;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private Connection connection;

    public RecipeRepository(Connection connection) {
        this.connection = connection;
    }

    public void addRecipe(Recipe recipe) {
        try (PreparedStatement psRecipe = connection.prepareStatement("INSERT INTO recipes(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psIngredients = connection.prepareStatement("INSERT INTO ingredients(name, recipe_id) VALUES (?, ?)")) {
            psRecipe.setString(1, recipe.getNameRecipe());
            psRecipe.execute();

            ResultSet rsRecipe = psRecipe.getGeneratedKeys();

            if (rsRecipe.next()) {
                final int recipeID = rsRecipe.getInt(1);
                for (String ingredient : recipe.getIngredients()) {
                    psIngredients.setString(1, ingredient);
                    psIngredients.setInt(2, recipeID);
                    psIngredients.execute();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipe(int id) {
        try (PreparedStatement psRecipe = connection.prepareStatement("DELETE FROM recipes WHERE id = ?")) {
            psRecipe.setInt(1, id);
            psRecipe.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM recipes")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                final int recipeID = rs.getInt("id");
                List<String> ingredients = getIngredientsByRecipeId(recipeID);

                Recipe recipe = new Recipe(recipeID, rs.getString("name"), ingredients);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    private List<String> getIngredientsByRecipeId(int id) {
        List<String> ingredients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT name FROM ingredients WHERE recipe_id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ingredients.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    @Nullable
    public Recipe findRecipeName(String name) {
        Recipe recipe = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM recipes WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                List<String> ingredients = getIngredientsByRecipeId(rs.getInt("id"));
                recipe = new Recipe(rs.getInt("id"), rs.getString("name"), ingredients);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    @Nullable
    public Recipe findRecipeByID(int id) {
        Recipe recipe = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM recipes WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                List<String> ingredients = getIngredientsByRecipeId(id);
                recipe = new Recipe(rs.getString("name"), ingredients);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}