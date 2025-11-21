package lv.pakit.dto.response.shipment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShipmentDeclarationStats {

    private final Long declarationId;
    private final String identifierCode;
    private final Double totalWeight;
    private final Double totalValue;
    private final Long packageAmount;
}
