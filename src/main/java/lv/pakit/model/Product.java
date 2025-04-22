package lv.pakit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "Product_Table")
@Entity
public class Product {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 3, max =500)
    private String title;

    @NotNull
    @Max(1000)
    @Min(1)
    private int quantity;

    @NotNull
    @Size(min = 3, max = 500)
    private String description;

    @NotNull
    @Size(min = 3, max = 50)
    private String category; //TODO Varbūt mainīt uz enum?

    @NotNull
    @Enumerated(EnumType.STRING)
    private Fragility fragility;

    @Builder
    public Product(String title, String description, int quantity, String category, Fragility fragility) {
        setTitle(title);
        setDescription(description);
        setQuantity(quantity);
        setCategory(category);
        setFragility(fragility);
    }
}
