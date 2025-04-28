package lv.pakit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Table(name = "commodity")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private int id;

    @OneToMany(mappedBy = "commodity")
    private List<PackageItem> packageItems;

    @NotNull
    private int commodity_code;

    @NotNull
    private String description;
}
