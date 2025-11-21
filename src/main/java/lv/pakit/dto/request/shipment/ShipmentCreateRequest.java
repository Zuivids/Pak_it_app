package lv.pakit.dto.request.shipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import lv.pakit.model.shipment.DeliveryState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Jacksonized
public class ShipmentCreateRequest {

    @NotBlank(message = "Required")
    private final String shipmentCode;
    @NotNull(message = "Required")
    private final LocalDateTime deliveryDatetime;
    @NotNull(message = "Required")
    private final DeliveryState deliveryState;
    @NotNull(message = "Required")
    private final List<Long> declarationIds;
}
