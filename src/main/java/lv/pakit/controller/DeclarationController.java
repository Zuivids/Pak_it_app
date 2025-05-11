package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.DeclarationDto;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.request.DeclarationRequest;
import lv.pakit.model.Commodity;
import lv.pakit.model.Declaration;
import lv.pakit.model.PackageItem;
import lv.pakit.service.DeclarationService;
import lv.pakit.service.PackageItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;
    private final PackageItemService packageItemService;

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        DeclarationDto declarationDto = declarationService.retriveById(id);
        List<PackageItemDto> packageItemDtos = packageItemService.retrieveByDeclarationId(id);

        model.addAttribute("declaration", declarationDto);
        model.addAttribute("packageItems", packageItemDtos);

        return "declaration-show-one-page";
    }

    @GetMapping("/declaration")
    public String getAllDeclarations(Model model) {
        model.addAttribute("declarations", declarationService.retriveAll());

        return "declaration-show-many-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationForm(Model model) {
        model.addAttribute("declaration", new Declaration());
        model.addAttribute("packageItem", new PackageItem());
        model.addAttribute("commodity", new Commodity());

        return "declaration-add-new-page";
    }

    @PostMapping("/declaration")
    public String saveDeclaration(@Valid @ModelAttribute DeclarationRequest declarationRequest) {
        declarationService.create(declarationRequest);

        return "redirect:/declaration";
    }

    @GetMapping("/declaration/{id}/edit")
    public String editDeclaration(@PathVariable("id") long id, Model model) {
        DeclarationDto declarationDto = declarationService.retriveById(id);
        model.addAttribute("declaration", declarationDto);

        return "declaration-edit-page";
    }

    @PostMapping("/declaration/{id}/edit")
    public String updateDeclaration(@PathVariable("id") long id, @Valid DeclarationDto declarationDto) {
        declarationService.updateById(id, declarationDto);

        return "redirect:/declaration";
    }

    @GetMapping("/declaration/{id}/delete")
    public String deleteDeclaration(@PathVariable("id") long id, Model model) {
        //TODO soft delete
        DeclarationDto declarationDto = declarationService.retriveById(id);
        model.addAttribute("declaration", declarationDto);

        return "declaration-delete-page";
    }

    @PostMapping("/declaration/{id}/delete")
    public String deletedDeclaration(@PathVariable("id") long id) {
        declarationService.deleteById(id);

        return "redirect:/declaration";
    }
}
