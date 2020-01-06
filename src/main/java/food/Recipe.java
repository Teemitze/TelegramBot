package food;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe implements Comparable<Recipe> {
    public Recipe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "name")
    String nameRecipe;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL)
    List<Ingredient> Ingredients;

    public Recipe(String nameRecipe, List<Ingredient> ingredients) {
        this.nameRecipe = nameRecipe;
        this.Ingredients = ingredients;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public List<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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
