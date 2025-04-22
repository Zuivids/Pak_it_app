package lv.pakit;

import lv.pakit.model.Category;
import lv.pakit.model.Fragility;
import lv.pakit.model.Product;
import lv.pakit.service.ProductService;
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
    public CommandLineRunner testDatabase(ProductService productService) {
        return args -> {
            productService.create(Product.builder()
                    .title("Telefons")
                    .description("Telefona apraksts")
                    .quantity(21)
                    .category(Category.Electronics)
                    .fragility(Fragility.FRAGILE)
                    .build());

            productService.create(Product.builder()
                    .title("Ziepes")
                    .description("Ziepju apraksts")
                    .quantity(14)
                    .category(Category.Cosmetics)
                    .fragility(Fragility.NON_FRAGILE)
                    .build());

            productService.create(Product.builder()
                    .title("Marinēti gurķi")
                    .description("Marinētu gurķu apraksts")
                    .quantity(133)
                    .category(Category.Food)
                    .fragility(Fragility.FRAGILE)
                    .build());
            System.out.println("===========================");
            //All Products
            ArrayList<Product> allProducts = productService.retriveAll();
            System.out.println("All products:");
            allProducts.forEach(System.out::println);
            System.out.println("---------------------------");

            //Quantity filter test
            System.out.println("Products with quantity less than 100:");
            productService.filterByQuantityLess(100).forEach(System.out::println);
            System.out.println("---------------------------");

            //Title and Description filter test
            System.out.println("Products with title or description containing 'Ziepes':");
            productService.filterByTitleOrDescription("%apr%").forEach(System.out::println);
            System.out.println("---------------------------");

            //Retrive service test
            System.out.println("Product with id eqaual to 2:");
            productService.retriveById(2);
            System.out.println(productService.retriveById(2));
            System.out.println("---------------------------");

            //Update service test
            System.out.println("Update 2nd product Quantity and Fragility:");
            Product p2 = Product.builder()
                    .title("Ziepes")
                    .description("Ziepju apraksts")
                    .quantity(444)
                    .category(Category.Cosmetics)
                    .fragility(Fragility.FRAGILE)
                    .build();
            productService.updateById(2, p2);
            System.out.println(productService.retriveById(2));
            System.out.println("---------------------------");

            //Delete service test
//            System.out.println("Delete 2nd product:");
//            crudService.deleteById(2);
//            System.out.println("---------------------------");
            System.out.println("Final products after filter and CRUD tests:");
            allProducts = productService.retriveAll();
            allProducts.forEach(System.out::println);
            System.out.println("===========================");
        };
    }

}
