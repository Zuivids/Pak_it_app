package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.IProductCRUDService;
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
    private final IProductCRUDService productCrudService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        try {
            Product product = productCrudService.retriveById(id);
            if (product != null) {
                model.addAttribute("product", product);
                return "product-show-one-page";
            } else {
                model.addAttribute("errorMessage", "Product not found with ID: " + id);
                return "error-page";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/product/all")
    public String getAllProducts(Model model) {
        try {
            System.out.println("Visi:" + productCrudService.retriveAll());
            model.addAttribute("mydata", productCrudService.retriveAll());
            return "product-show-many-page";
        } catch (Exception e) {
            model.addAttribute("mydata", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/product/add")
    public String addProductById(Model model) {
        model.addAttribute("product", new Product());

        return "product-add-new-page";
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product) {
        productCrudService.create(product);
        return "redirect:/product/all";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable("id") int id, Model model) throws Exception {
        Product product = productCrudService.retriveById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-edit-page";
        } else {
            model.addAttribute("errorMessage", "Product not found with ID: " + id);
            return "error-page";
        }
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable("id") int id, @Valid Product product, BindingResult result,
                                Model model) {
        try {
            productCrudService.updateById(id, product);
            return "redirect:/product/all";
        } catch (Exception e) {
            model.addAttribute("mydata", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model) throws Exception {
        //TODO soft delete
        Product product = productCrudService.retriveById(id);

        if (product != null) {
            model.addAttribute("product", product);
            return "product-delete-page";
        } else {
            model.addAttribute("errorMessage", "Product not found with ID: " + id);
            return "error-page";
        }
    }

    @PostMapping("/product/deleted/{id}")
    public String deletedProduct(@PathVariable("id") int id, Model model) {
        try {
            productCrudService.deleteById(id);
            return "redirect:/product/all";
        } catch (Exception e) {
            model.addAttribute("mydata", e.getMessage());
            return "error-page";
        }
    }

}