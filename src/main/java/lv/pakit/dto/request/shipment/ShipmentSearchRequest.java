package lv.pakit.dto.request.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lv.pakit.model.shipment.DeliveryState;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ShipmentSearchRequest {

    private final String shipmentCode;
    private final LocalDate deliveryDate;
    private final DeliveryState deliveryState;
}
