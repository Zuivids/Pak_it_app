package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.shipment.ShipmentCreateRequest;
import lv.pakit.service.shipment.ShipmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipment")
public class ShipmentRestController {

    private final ShipmentService shipmentService;

    @PostMapping
    public void saveShipment(@Valid @RequestBody ShipmentCreateRequest shipmentCreateRequest) {
        shipmentService.create(shipmentCreateRequest);
    }

    @PutMapping("/{id}")
    public void updateShipment(@PathVariable("id") long id, @Valid @RequestBody ShipmentCreateRequest declarationRequest) {
        shipmentService.updateById(id, declarationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteShipment(@PathVariable("id") long id) {
        shipmentService.deleteById(id);
    }
}
