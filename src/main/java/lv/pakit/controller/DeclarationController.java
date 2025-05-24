//package lv.pakit.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lv.pakit.controller.page.BasePageController;
//import lv.pakit.dto.DeclarationDto;
//import lv.pakit.dto.PackageItemDto;
//import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
//import lv.pakit.service.ClientService;
//import lv.pakit.service.CommodityService;
//import lv.pakit.service.DeclarationService;
//import lv.pakit.service.PackageItemService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class DeclarationController extends BasePageController {
//
//    private final DeclarationService declarationService;
//    private final PackageItemService packageItemService;
//    private final ClientService clientService;
//    private final CommodityService commodityService;
//
//    @GetMapping("/declaration/{id}")
//    public String getDeclarationById(@PathVariable long id, Model model) {
//        return handleRequest(() -> {
//            DeclarationDto declarationDto = declarationService.retriveById(id);
//            List<PackageItemDto> packageItemDtos = packageItemService.retrieveByDeclarationId(id);
//
//            model.addAttribute("declaration", declarationDto);
//            model.addAttribute("packageItems", packageItemDtos);
//        }, "declaration-show-one-page", "declaration-show-one-page", model);
//    }
//
//    @GetMapping("/declaration")
//    public String searchDeclarations(DeclarationSearchRequest request, Model model) {
//        return handleRequest(() -> {
//            List<DeclarationDto> results = declarationService.search(request);
//            model.addAttribute("declarations", results);
//        }, "declaration-show-many-page", "declaration-show-many-page", model);
//    }
//
//    @GetMapping("/declaration/new")
//    public String showDeclarationForm(Model model) {
//        return handleRequest(() -> {
//            model.addAttribute("declaration", declarationService.defaultDeclaration());
//            model.addAttribute("commodities", commodityService.retrieveAll());
//            model.addAttribute("clients", clientService.retrieveAll());
//        }, "declaration-add-new-page", "declaration-add-new-page", model);
//    }
//
//    @PostMapping("/declaration")
//    public String saveDeclaration(@Valid @ModelAttribute("declaration") DeclarationDto declarationDto,
//                                  BindingResult bindingResult, Model model) {
//        model.addAttribute("commodities", commodityService.retrieveAll());
//        model.addAttribute("clients", clientService.retrieveAll());
//        return handleRequest(() -> {
//            declarationService.create(declarationDto);
//        }, "redirect:/declaration", "declaration-add-new-page", model, bindingResult);
//    }
//
//    @GetMapping("/declaration/{id}/edit")
//    public String editDeclaration(@PathVariable("id") long id, Model model) {
//        return handleRequest(() -> {
//            DeclarationDto declarationDto = declarationService.retriveById(id);
//            model.addAttribute("declaration", declarationDto);
//        }, "declaration-edit-page", "declaration-edit-page", model);
//    }
//
//    @PostMapping("/declaration/{id}/edit")
//    public String updateDeclaration(@PathVariable("id") long id, @Valid @ModelAttribute("declaration") DeclarationDto declarationDto,
//                                    BindingResult bindingResult, Model model) {
//        return handleRequest(() -> {
//            declarationService.updateById(id, declarationDto);
//        }, "redirect:/declaration", "declaration-edit-page", model, bindingResult);
//    }
//
//    @GetMapping("/declaration/{id}/delete")
//    public String deleteDeclaration(@PathVariable("id") long id, Model model) {
//        //TODO soft
//        return handleRequest(() -> {
//            DeclarationDto declarationDto = declarationService.retriveById(id);
//            model.addAttribute("declaration", declarationDto);
//        }, "declaration-delete-page", "declaration-delete-page", model);
//    }
//
//    @PostMapping("/declaration/{id}/delete")
//    public String deletedDeclaration(@PathVariable("id") long id, Model model) {
//        return handleRequest(() -> {
//            declarationService.deleteById(id);
//        }, "redirect:/declaration", "declaration-delete-page", model);
//    }
//}
