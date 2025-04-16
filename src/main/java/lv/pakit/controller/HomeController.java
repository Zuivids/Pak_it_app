package lv.pakit.controller;

import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.IProductCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    private final IProductRepo productRepo;
    private final IProductCRUDService crudService;

    @Autowired
    public HomeController(IProductRepo productRepo, IProductCRUDService crudService) {
        this.productRepo = productRepo;
        this.crudService = crudService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        try {
            Product product = crudService.retriveById(id);
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
//            throw new NullPointerException("Force Null Pointer Exception");
            System.out.println("Visi:" + crudService.retriveAll());
            model.addAttribute("mydata", crudService.retriveAll());
            return "product-show-many-page";
        } catch (Exception e) {
            model.addAttribute("mydata", e.getMessage());
            return "error-page";
        }
    }
}