package lv.pakit.controller.page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.DeclarationService;
import lv.pakit.service.PackageItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DeclarationPageController extends BasePageController {

    private final DeclarationService declarationService;
    private final CommodityService commodityService;
    private final ClientService clientService;

    @GetMapping("/declaration")
    public String getAllDeclarations(@Valid @ModelAttribute(value = "query") DeclarationSearchRequest request,
                                     BindingResult bindingResult, Model model) {
        return handleErrors(
            () -> model.addAttribute("declarations", declarationService.search(request)),
            "declaration-show-many-page",
            model,
            bindingResult
        );
    }

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        addDeclarationToModel(id, model);
        return "declaration-show-one-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationCreateForm(Model model) {
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration-add-new-page";
    }

    @GetMapping("/declaration/{id}/edit")
    public String showDeclarationEditForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());
        return "declaration-edit-page";
    }

    @GetMapping("/declaration/{id}/delete")
    public String showDeclarationDeleteForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);
        return "declaration-delete-page";
    }

    private void addDeclarationToModel(long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
    }
}
