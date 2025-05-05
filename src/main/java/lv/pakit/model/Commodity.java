package lv.pakit.model;

import jakarta.persistence.*;
import lombok.*;

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
    private long commodityId;
    private String commodityCode;
    private String description;
}
