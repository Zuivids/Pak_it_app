package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.request.PackageItemRequest;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
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
public class PackageItemController {

    private final IPackageItemRepo packageItemRepo;
    private final PackageItemService packageItemService;


    @GetMapping("/packageitem/{id}")
    public String getPackageItemById(@PathVariable int id, Model model) {
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
        return "package-item-add-new-page";
    }

    @PostMapping("/packageitem")
    public String savePackageItem(@Valid @ModelAttribute PackageItemRequest packageItemRequest) {
        packageItemService.create(packageItemRequest);
        return "redirect:/packageitem";
    }

    @GetMapping("/packageitem/{packageItemId}/edit")
    public String editPackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        model.addAttribute("packageItem", new PackageItem());

        return "package-item-edit-page";
    }

    @PostMapping("/packageitem/{packageItemId}/edit")
    public String updatePackageItem(@PathVariable("packageItemId") int packageItemId, @Valid PackageItemDto packageItemDto, BindingResult result,
                                    Model model) {
        packageItemService.updateById(packageItemId, packageItemDto);

        return "redirect:/packageitem/all";
    }

    @GetMapping("/packageitem/{packageItemId}/delete")
    public String deletePackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        //TODO soft delete
        PackageItemDto packageItemDto = packageItemService.retrieveById(packageItemId);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-delete-page";
    }

    @PostMapping("/packageitem/{packageItemId}/deleted")
    public String deletedPackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        packageItemService.deleteById(packageItemId);

        return "redirect:/packageitem";
    }
}
