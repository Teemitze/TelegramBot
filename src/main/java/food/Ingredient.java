package food;

import javax.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    public Ingredient() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ingredientId;

    @Column(name = "name")
    String nameIngredient;

    public Ingredient(String nameIngredient, Recipe recipe) {
        this.nameIngredient = nameIngredient;
        this.recipe = recipe;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id", nullable = false)
    Recipe recipe;

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getNameIngredient() {
        return nameIngredient;
    }

    public void setNameIngredient(String nameIngredient) {
        this.nameIngredient = nameIngredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
