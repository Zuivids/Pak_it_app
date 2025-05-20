package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.model.PackageItem;
import lv.pakit.service.CommodityService;
import lv.pakit.service.PackageItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PackageItemController extends BaseController {

    private final PackageItemService packageItemService;
    private final CommodityService commodityService;

    @GetMapping("/packageitem/{id}")
    public String getPackageItemById(@PathVariable long id, Model model) {
        return handleRequest(() -> {
            PackageItemDto packageItemDto = packageItemService.retrieveById(id);
            model.addAttribute("packageItem", packageItemDto);
        }, "package-item-show-one-page", "package-item-show-one-page", model);
    }

    @GetMapping("/packageitem")
    public String getAllPackageItems(Model model) {
        return handleRequest(() -> {
            model.addAttribute("packageItems", packageItemService.retrieveAll());
        }, "package-item-show-many-page", "package-item-show-many-page", model);
    }

    @GetMapping("/packageitem/new")
    public String showPackageItemForm(Model model) {
        return handleRequest(() -> {
            model.addAttribute("packageItem", new PackageItemDto());
            model.addAttribute("commodities", commodityService.retrieveAll());
        }, "package-item-add-new-page", "package-item-add-new-page", model);
    }

    @PostMapping("/packageitem")
    public String savePackageItem(@Valid @ModelAttribute("packageitem") PackageItemDto packageItemDto,
                                  BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            packageItemService.create(packageItemDto);
        }, "redirect:/packageitem", "package-item-add-new-page", model, bindingResult);
    }

    @GetMapping("/packageitem/{id}/edit")
    public String editPackageItem(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            PackageItemDto packageItemDto = packageItemService.retrieveById(id);
            model.addAttribute("packageItem", packageItemDto);
        }, "package-item-edit-page", "package-item-edit-page", model);
    }

    @PostMapping("/packageitem/{id}/edit")
    public String updatePackageItem(@PathVariable("id") long id, @Valid PackageItemDto packageItemDto,
                                    BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            packageItemService.updateById(id, packageItemDto);
        }, "redirect:/packageitem", "package-item-edit-page", model, bindingResult);
    }

    @GetMapping("/packageitem/{id}/delete")
    public String deletePackageItem(@PathVariable("id") long id, Model model) {
        //TODO soft delete
        return handleRequest(() -> {
            PackageItemDto packageItemDto = packageItemService.retrieveById(id);
            model.addAttribute("packageItem", packageItemDto);
        }, "package-item-delete-page", "package-item-delete-page", model);
    }

    @PostMapping("/packageitem/{id}/delete")
    public String deletedPackageItem(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            packageItemService.deleteById(id);
        }, "redirect:/packageitem", "packageitem-delete-page", model);

    }
}
