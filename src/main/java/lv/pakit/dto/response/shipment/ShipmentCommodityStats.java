package lv.pakit.dto.response.shipment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShipmentCommodityStats {

    private final Long commodityId;
    private final String commodityCode;
    private final String description;
    private final Double totalWeight;
    private final Double totalValue;
}
