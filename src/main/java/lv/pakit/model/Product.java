package lv.pakit.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

    @Setter(value = AccessLevel.NONE)
    private int id;
    private String title;
    private int quantity;
    private String description;
    private String type; //TODO Varbūt mainīt uz enum?
    //TODO enum ""/ trausls

    private static int counter = 0;

    public void setId(){
        id = counter;
        counter++;
    }

    public Product(String title, String description, int quantity, String type) {
        setId();
        setTitle(title);
        setDescription(description);
        setQuantity(quantity);
        setType(type);
    }
}
