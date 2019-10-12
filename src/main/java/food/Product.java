package food;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    int id;
    String name;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    List<Ingredient> products;

}
