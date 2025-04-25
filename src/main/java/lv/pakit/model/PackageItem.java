package lv.pakit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Table(name = "package_item")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Integer packageItemId;

    @NotNull
    private int commodityId;

    @NotNull
    private int declarationId;

    @NotNull
    private int quantity;

    @NotNull
    private double netWeight;

    @NotNull
    private double value;

    @NotNull
    private boolean used;
}
