package lv.pakit.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageItemDto {

    private long packageItemId;
    private int quantity;
    private double netWeight;
    private double value;
    private boolean used;
    private String newCommodityDescription;
    private CommodityDto commodity;
    private long declarationId;
}
