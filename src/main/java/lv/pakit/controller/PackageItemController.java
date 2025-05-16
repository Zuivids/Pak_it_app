package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.request.PackageItemRequest;
import lv.pakit.model.PackageItem;
import lv.pakit.service.CommodityService;
import lv.pakit.service.PackageItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PackageItemController {

    private final PackageItemService packageItemService;
    private final CommodityService commodityService;

    @GetMapping("/packageitem/{id}")
    public String getPackageItemById(@PathVariable long id, Model model) {
        PackageItemDto packageItemDto = packageItemService.retrieveById(id);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-show-one-page";
    }

    @GetMapping("/packageitem")
    public String getAllPackageItems(Model model) {
        model.addAttribute("packageItems", packageItemService.retrieveAll());

        return "package-item-show-many-page";
    }

    @GetMapping("/packageitem/new")
    public String showPackageItemForm(Model model) {
        model.addAttribute("packageItem", new PackageItem());
        model.addAttribute("commodities", commodityService.retrieveAll());
        return "package-item-add-new-page";
    }

    @PostMapping("/packageitem")
    public String savePackageItem(@Valid @ModelAttribute PackageItemRequest packageItemRequest) {
        packageItemService.create(packageItemRequest);

        return "redirect:/packageitem";
    }

    @GetMapping("/packageitem/{id}/edit")
    public String editPackageItem(@PathVariable("id") long id, Model model) {
        PackageItemDto packageItemDto = packageItemService.retrieveById(id);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-edit-page";
    }

    @PostMapping("/packageitem/{id}/edit")
    public String updatePackageItem(@PathVariable("id") long id, @Valid PackageItemRequest packageItemRequest) {
        packageItemService.updateById(id, packageItemRequest);

        return "redirect:/packageitem";
    }

    @GetMapping("/packageitem/{id}/delete")
    public String deletePackageItem(@PathVariable("id") long id, Model model) {
        //TODO soft delete
        PackageItemDto packageItemDto = packageItemService.retrieveById(id);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-delete-page";
    }

    @PostMapping("/packageitem/{id}/delete")
    public String deletedPackageItem(@PathVariable("id") long id, Model model) {
        packageItemService.deleteById(id);

        return "redirect:/packageitem";
    }
}
