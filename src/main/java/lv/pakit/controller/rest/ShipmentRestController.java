package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.dto.request.shipment.ShipmentCreateRequest;
import lv.pakit.service.shipment.ShipmentService;
import org.springframework.web.bind.annotation.*;

import static lv.pakit.model.user.UserRole.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipment")
public class ShipmentRestController {

    private final ShipmentService shipmentService;

    @PostMapping
    @RequiresRole(ADMIN)
    public void saveShipment(@Valid @RequestBody ShipmentCreateRequest shipmentCreateRequest) {
        shipmentService.create(shipmentCreateRequest);
    }

    @PutMapping("/{id}")
    @RequiresRole(ADMIN)
    public void updateShipment(@PathVariable("id") long id, @Valid @RequestBody ShipmentCreateRequest declarationRequest) {
        shipmentService.updateById(id, declarationRequest);
    }

    @DeleteMapping("/{id}")
    @RequiresRole(ADMIN)
    public void deleteShipment(@PathVariable("id") long id) {
        shipmentService.deleteById(id);
    }
}
