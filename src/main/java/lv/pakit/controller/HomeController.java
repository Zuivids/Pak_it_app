package lv.pakit.controller;

import lv.pakit.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        Product product = new Product("Jaka", "Jakas apraksts", 250, "Apģērbs");
        model.addAttribute("data", product);
        return "product-show-one-page";
    }

    @GetMapping("/product/list")
    public String getProductList(Model model) {

       List<Product> allProducts = Arrays.asList(
                new Product("Telefons", "Telefona apraksts", 21, "Elektroprece"),
                new Product("Ziepes", "Ziepju apraksts", 14, "Higēnas prece"),
                new Product("Marinēti gurķi", "Marinētu gurķu apraksts", 133, "Pārtika")
        );
        model.addAttribute("data",allProducts);
        return "product-show-many-page";
    }

}
