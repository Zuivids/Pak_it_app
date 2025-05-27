package lv.pakit.dto.response;

import lombok.*;
import lv.pakit.model.Commodity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "packageItemId")
public class PackageItemResponse {

    private long packageItemId;
    private CommodityResponse commodity;
    private long declarationId;
    private int quantity;
    private double netWeight;
    private double value;
    private boolean used;
}
