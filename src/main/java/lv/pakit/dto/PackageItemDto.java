package lv.pakit.dto;

import lombok.*;

@Getter
@Builder
public class PackageItemDto {

    private long packageItemId;
    private int quantity;
    private double netWeight;
    private double value;
    private boolean used;

    private CommodityDto commodity;
    //TODO add declaration
}
