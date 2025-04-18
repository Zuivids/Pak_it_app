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
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 3, max =500)
    @Pattern(regexp = "[A-ZĒŪĪĶĻĢŠĀŽČŅa-zēūīķļģšāžčņ\\s\\d]+")
    @Column(name = "Title")
    private String title;

    @NotNull
    @Max(1000)
    @Min(1)
    @Column(name = "Quantity")
    private int quantity;

    @NotNull
    @Size(min = 3, max = 500)
    @Pattern(regexp = "[A-ZĒŪĪĶĻĢŠĀŽČŅa-zēūīķļģšāžčņ\\s\\d]+")
    @Column(name = "Description")
    private String description;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[A-ZĒŪĪĶĻĢŠĀŽČŅa-zēūīķļģšāžčņ\\s\\d]+")
    @Column(name = "Type")
    private String type; //TODO Varbūt mainīt uz enum?

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Fragility")
    private Fragility fragility;

    public Product(String title, String description, int quantity, String type, Fragility fragility) {
        setTitle(title);
        setDescription(description);
        setQuantity(quantity);
        setType(type);
        setFragility(fragility);
    }
}
