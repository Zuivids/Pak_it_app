package lv.pakit.controller.page;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.shipment.ShipmentSearchRequest;
import lv.pakit.dto.response.shipment.ShipmentResponse;
import lv.pakit.service.shipment.ShipmentService;
import lv.pakit.service.shipment.ShipmentStatsExportService;
import lv.pakit.service.shipment.ShipmentStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentPageController {

    private final ShipmentService shipmentService;
    private final ShipmentStatsService shipmentStatsService;
    private final ShipmentStatsExportService shipmentStatsExportService;

    @GetMapping
    public String searchShipments(@ModelAttribute(value = "query") ShipmentSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<ShipmentResponse> shipmentPage = shipmentService.searchPaged(
                request, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt")));

        int totalPages = shipmentPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        model.addAttribute("shipments", shipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
