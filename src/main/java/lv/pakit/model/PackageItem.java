package lv.pakit.model;

import jakarta.persistence.*;
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
    private long packageItemId;
    @ManyToOne
    @JoinColumn(name = "commodity_id")
    private Commodity commodity;
    private int declarationId;
    private int quantity;
    private double netWeight;
    private double value;
    private boolean used;
}
