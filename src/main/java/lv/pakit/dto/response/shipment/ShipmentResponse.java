package lv.pakit.dto.response.shipment;

import lombok.Builder;
import lombok.Getter;
import lv.pakit.model.shipment.DeliveryState;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShipmentResponse {

    private final Long shipmentId;
    private final String shipmentCode;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime deliveryDatetime;
    private final DeliveryState deliveryState;
}
