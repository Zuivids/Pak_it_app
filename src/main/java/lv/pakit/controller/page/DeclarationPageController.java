package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.DeclarationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DeclarationPageController {

    private final DeclarationService declarationService;
    private final CommodityService commodityService;
    private final ClientService clientService;

    @GetMapping("/declaration")
    public String getAllDeclarations(
            @RequestParam(required = false) String identifierCode,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String senderName,
            @RequestParam(required = false) String senderAddress,
            @RequestParam(required = false) String senderCountryCode,
            @RequestParam(required = false) String senderPhoneNumber,
            @RequestParam(required = false) String receiverName,
            @RequestParam(required = false) String receiverAddress,
            @RequestParam(required = false) String receiverCountryCode,
            @RequestParam(required = false) String receiverPhoneNumber,
            @RequestParam(required = false) Double totalWeight,
            @RequestParam(required = false) Double totalValue,
            @RequestParam(required = false) String date,
            Model model
    ) {
        DeclarationSearchRequest request = DeclarationSearchRequest.builder()
                .identifierCode(identifierCode)
                .clientName(clientName)
                .senderName(senderName)
                .senderAddress(senderAddress)
                .senderCountryCode(senderCountryCode)
                .senderPhoneNumber(senderPhoneNumber)
                .receiverName(receiverName)
                .receiverAddress(receiverAddress)
                .receiverCountryCode(receiverCountryCode)
                .receiverPhoneNumber(receiverPhoneNumber)
                .totalWeight(totalWeight)
                .totalValue(totalValue)
                .date(date)
                .build();

        model.addAttribute("declarations", declarationService.search(request));
        return "declaration-show-many-page";
    }

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
        return "declaration-show-one-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationCreateForm(Model model) {
        model.addAttribute("declaration", declarationService.defaultDeclaration());
        model.addAttribute("commodities", commodityService.fetchAll());
//        model.addAttribute("clients", clientService.fetchAll()); //TODO add dropdown button for clientsFullName

        return "declaration-add-new-page";
    }

    @GetMapping("/declaration/{id}/edit")
    public String showDeclarationEditForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
        return "declaration-edit-page";
    }

    @GetMapping("/declaration/{id}/delete")
    public String showDeclarationDeleteForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
        return "declaration-delete-page";
    }
}
