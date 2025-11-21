package lv.pakit.dto.response.shipment;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ShipmentStatsResponse {

    private final Double totalWeight;
    private final Double totalValue;
    private final Long totalPackageAmount;
    private final List<ShipmentDeclarationStats> declarationStats;
    private final List<ShipmentCommodityStats> commodityStats;
}
