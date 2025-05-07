package lv.pakit.controller;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.DeclarationDto;
import lv.pakit.service.ClientService;
import lv.pakit.service.DeclarationService;
import lv.pakit.service.PackageItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        DeclarationDto declarationDto = declarationService.retriveById(id);
        model.addAttribute("declaration", declarationDto);

        return "declaration-show-one-page";
    }

    @GetMapping("/declaration")
    public String getAllDeclarations(Model model) {
        model.addAttribute("declarations", declarationService.retriveAll());

        return "declaration-show-many-page";
    }
}
