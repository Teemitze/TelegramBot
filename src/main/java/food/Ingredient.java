package food;

import javax.persistence.*;

@Table(name = "ingredients")
@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    int id;
    int amount;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    public Ingredient(int amount, Recipe recipe, Product product) {
        this.amount = amount;
        this.recipe = recipe;
        this.product = product;
    }
}
