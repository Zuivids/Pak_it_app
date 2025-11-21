package lv.pakit.controller.page;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.shipment.ShipmentSearchRequest;
import lv.pakit.service.shipment.ShipmentService;
import lv.pakit.service.shipment.ShipmentStatsExportService;
import lv.pakit.service.shipment.ShipmentStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentPageController {

    private final ShipmentService shipmentService;
    private final ShipmentStatsService shipmentStatsService;
    private final ShipmentStatsExportService shipmentStatsExportService;

    @GetMapping
    public String searchShipments(@ModelAttribute(value = "query") ShipmentSearchRequest request, Model model) {
        model.addAttribute("shipments", shipmentService.search(request));

        return "shipment/shipment-show-many-page";
    }

    @GetMapping("/new")
    public String addNewShipment(Model model) {
        return "shipment/shipment-add-new-page";
    }

    @GetMapping("/{id}")
    public String viewShipment(@PathVariable long id, Model model) {
        model.addAttribute("shipment", shipmentService.fetchById(id));
        model.addAttribute("shipmentStats", shipmentStatsService.getShipmentStats(id));

        return "shipment/shipment-show-one-page";
    }

    @GetMapping("/{id}/edit")
    public String editShipment(@PathVariable long id, Model model) {
        model.addAttribute("shipment", shipmentService.fetchById(id));

        return "shipment/shipment-edit-page";
    }

    @GetMapping("/{id}/delete")
    public String deleteShipment(@PathVariable long id, Model model) {
        model.addAttribute("shipment", shipmentService.fetchById(id));

        return "shipment/shipment-delete-page";
    }

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable long id, HttpServletResponse response) {
        shipmentStatsExportService.getShipmentStatsPdf(id, response);
    }
}
