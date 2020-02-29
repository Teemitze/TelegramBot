package food;

import java.util.List;

public class Recipe implements Comparable<Recipe> {
    int id;
    String nameRecipe;
    List<String> Ingredients;

    public Recipe() {
    }

    public Recipe(String nameRecipe, List<String> ingredients) {
        this.nameRecipe = nameRecipe;
        this.Ingredients = ingredients;
    }

    public Recipe(int id, String nameRecipe, List<String> ingredients) {
        this.id = id;
        this.nameRecipe = nameRecipe;
        this.Ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public List<String> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        Ingredients = ingredients;
    }

    @Override
    public int compareTo(Recipe recipe) {
        if (this.getId() > recipe.getId()) {
            return 1;
        } else {
            return -1;
        }
    }
}
