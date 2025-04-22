package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.ProductDto;
import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final IProductRepo productRepo;
    private final ProductService productService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

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
    public String editProductForm(@PathVariable("id") int id, Model model) throws Exception {
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
    public String deleteProduct(@PathVariable("id") int id, Model model) throws Exception {
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

}