package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.ProductDto;
import lv.pakit.model.PackageItem;
import lv.pakit.model.Product;
import lv.pakit.repo.IPackageItemRepo;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.PackageItemService;
import lv.pakit.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final IProductRepo productRepo;
    private final ProductService productService;
    private final IPackageItemRepo packageItemRepo;
    private final PackageItemService packageItemService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    //    ==================Product==========================

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        ProductDto productDto = productService.retrieveById(id);
        model.addAttribute("product", productDto);

        return "product-show-one-page";
    }

    @GetMapping("/product/all")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.retrieveAll());

        return "product-show-many-page";
    }

    @GetMapping("/product/add")
    public String addProductById(Model model) {
        model.addAttribute("product", new Product());

        return "product-add-new-page";
    }

    @PostMapping("/product/save")
    public String saveProduct(@Valid @ModelAttribute ProductDto productDto) { //prodcutdto + valid
        productService.create(productDto);

        return "redirect:/product/all";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", new Product());

        return "product-edit-page";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable("id") int id, @Valid ProductDto productDto, BindingResult result,
                                Model model) {
        productService.updateById(id, productDto);

        return "redirect:/product/all";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model) {
        //TODO soft delete
        ProductDto product = productService.retrieveById(id);
        model.addAttribute("product", product);

        return "product-delete-page";
    }

    @PostMapping("/product/deleted/{id}")
    public String deletedProduct(@PathVariable("id") int id, Model model) {
        productService.deleteById(id);

        return "redirect:/product/all";
    }

    //    ==================PackageItem==========================

    @GetMapping("/packageitem/{id}")
    public String getPackageItemById(@PathVariable int id, Model model) {
        PackageItemDto packageItemDto = packageItemService.retrieveById(id);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-show-one-page";
    }

    @GetMapping("/packageitem/all")
    public String getAllPackageItems(Model model) {
        model.addAttribute("packageItems", packageItemService.retrieveAll());

        return "package-item-show-many-page";
    }

    @GetMapping("/packageitem/new")
    public String showPackageItemForm(Model model) {
        model.addAttribute("packageItem", new PackageItem());
        return "package-item-add-new-page";
    }

    @PostMapping("/packageitem/save")
    public String savePackageItem(@Valid @ModelAttribute PackageItemDto packageItemDto) {
        packageItemService.create(packageItemDto);
        return "redirect:/packageitem/all";
    }

    @GetMapping("/packageitem/edit/{packageItemId}")
    public String editPackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        model.addAttribute("packageItem", new PackageItem());

        return "package-item-edit-page";
    }

    @PostMapping("/packageitem/update/{packageItemId}")
    public String updatePackageItem(@PathVariable("packageItemId") int packageItemId, @Valid PackageItemDto packageItemDto, BindingResult result,
                                    Model model) {
        packageItemService.updateById(packageItemId, packageItemDto);

        return "redirect:/packageitem/all";
    }

    @GetMapping("/packageitem/delete/{packageItemId}")
    public String deletePackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        //TODO soft delete
        PackageItemDto packageItemDto = packageItemService.retrieveById(packageItemId);
        model.addAttribute("packageItem", packageItemDto);

        return "package-item-delete-page";
    }

    @PostMapping("/packageitem/deleted/{packageItemId}")
    public String deletedPackageItem(@PathVariable("packageItemId") int packageItemId, Model model) {
        packageItemService.deleteById(packageItemId);

        return "redirect:/product/all";
    }
//    ==================Commodity==========================

//    ==================Decleration==========================
}