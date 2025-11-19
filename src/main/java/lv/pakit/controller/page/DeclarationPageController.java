package lv.pakit.controller.page;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.shipment.ShipmentService;
import lv.pakit.service.declaration.DeclarationExportService;
import lv.pakit.service.declaration.DeclarationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/declaration")
@RequiredArgsConstructor
public class DeclarationPageController {

    private final DeclarationService declarationService;
    private final DeclarationExportService declarationExportService;
    private final CommodityService commodityService;
    private final ClientService clientService;
    private final ShipmentService shipmentService;

    @GetMapping
    public String getAllDeclarations(@ModelAttribute(value = "query") DeclarationSearchRequest request,
                                     BindingResult bindingResult, Model model) {
        model.addAttribute("declarations", declarationService.search(request));

        return "declaration/declaration-show-many-page";
    }

    @GetMapping("/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        addDeclarationToModel(id, model);
        return "declaration/declaration-show-one-page";
    }

    @GetMapping("/new")
    public String showDeclarationCreateForm(Model model) {
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration/declaration-add-new-page";
    }

    @GetMapping("/{id}/edit")
    public String showDeclarationEditForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());
        model.addAttribute("shipments", shipmentService.fetchAll());

        return "declaration/declaration-edit-page";
    }

    @GetMapping("/{id}/delete")
    public String showDeclarationDeleteForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);

        return "declaration/declaration-delete-page";
    }

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable long id, HttpServletResponse response) {
        declarationExportService.getDeclarationPdf(id, response);
    }

    private void addDeclarationToModel(long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
    }
}
