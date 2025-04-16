package lv.pakit.controller;

import lv.pakit.model.Product;
import lv.pakit.model.Fragility;
import lv.pakit.repo.IProductRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    private final IProductRepo productRepo;

    public HomeController(IProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        Product product = productRepo.findById(1).orElse(null);
        model.addAttribute("data", product);
        return "product-show-one-page";
    }

    @GetMapping("/product/list")
    public String getProductList(Model model) {

        List<Product> allProducts = (List<Product>) productRepo.findAll();
        model.addAttribute("data", allProducts);
        return "product-show-many-page";
    }

}
