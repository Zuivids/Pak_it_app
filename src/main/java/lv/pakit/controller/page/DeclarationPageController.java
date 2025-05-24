package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
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

    @GetMapping("/declaration")
    public String getAllDeclarations(@RequestParam(required = false) DeclarationSearchRequest request, Model model) {
        model.addAttribute("declarations", declarationService.search(request));
        return "declaration-show-many-page";
    }

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
        return "declaration-show-one-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationCreateForm() {
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
