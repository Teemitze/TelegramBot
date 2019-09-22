package food;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {
    public Recipe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "name")
    String nameRecipe;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
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
}
