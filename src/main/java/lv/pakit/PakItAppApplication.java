package lv.pakit;

import lv.pakit.model.Fragility;
import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.IProductCRUDService;
import lv.pakit.service.IProductFilteringService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class PakItAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PakItAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner testDatabase(IProductCRUDService crudService, IProductFilteringService filteringService) {
        return args -> {
            crudService.create(new Product("Telefons", "Telefona apraksts", 21, "Elektroprece", Fragility.FRAGILE));
            crudService.create(new Product("Ziepes", "Ziepju apraksts", 14, "Higēnas prece", Fragility.NON_FRAGILE));
            crudService.create(new Product("Marinēti gurķi", "Marinētu gurķu apraksts", 133, "Pārtika", Fragility.FRAGILE));

            System.out.println("===========================");
            //All Products
            ArrayList<Product> allProducts = crudService.retriveAll();
            System.out.println("All products:");
            allProducts.forEach(System.out::println);
            System.out.println("---------------------------");

            //Quantity filter test
            System.out.println("Products with quantity less than 100:");
            filteringService.filterByQuantityLess(100).forEach(System.out::println);
            System.out.println("---------------------------");

            //Title and Description filter test
            System.out.println("Products with title or description containing 'Ziepes':");
            filteringService.filterByTitleOrDescription("%apr%").forEach(System.out::println);
            System.out.println("---------------------------");

            //Retrive service test
            System.out.println("Product with id eqaual to 2:");
            crudService.retriveById(2);
            System.out.println(crudService.retriveById(2));
            System.out.println("---------------------------");

            //Update service test
            System.out.println("Update 2nd product Quantity and Fragility:");
            Product p2 = new Product("Ziepes", "Ziepju apraksts", 444, "Higēnas prece", Fragility.FRAGILE);
            crudService.updateById(2, p2);
            System.out.println(crudService.retriveById(2));
            System.out.println("---------------------------");

            //Delete service test
            System.out.println("Delete 2nd product:");
            crudService.deleteById(2);
            System.out.println("---------------------------");
            System.out.println("Final products after filter and CRUD tests:");
            allProducts = crudService.retriveAll();
            allProducts.forEach(System.out::println);
            System.out.println("===========================");
        };
    }

}
