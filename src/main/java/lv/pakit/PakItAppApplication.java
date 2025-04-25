package lv.pakit;

import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.ProductDto;
import lv.pakit.model.ProductFragility;
import lv.pakit.model.ProductCategory;
import lv.pakit.model.Product;
import lv.pakit.service.PackageItemService;
import lv.pakit.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PakItAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PakItAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner testProductDatabaseLinkage(ProductService productService) {
        return args -> {
            productService.create(ProductDto.builder()
                    .title("Telefons")
                    .description("Telefona apraksts")
                    .quantity(21)
                    .category(ProductCategory.ELECTRONICS.getPublicName())
                    .fragility(ProductFragility.FRAGILE.getPublicName())
                    .build());

            productService.create(ProductDto.builder()
                    .title("Ziepes")
                    .description("Ziepju apraksts")
                    .quantity(14)
                    .category(ProductCategory.COSMETICS.getPublicName())
                    .fragility(ProductFragility.NON_FRAGILE.getPublicName())
                    .build());

            productService.create(ProductDto.builder()
                    .title("Marinēti gurķi")
                    .description("Marinētu gurķu apraksts")
                    .quantity(133)
                    .category(ProductCategory.FOOD.getPublicName())
                    .fragility(ProductFragility.FRAGILE.getPublicName())
                    .build());
            System.out.println("===========================");
            //All Products
            List<ProductDto> allProducts = productService.retrieveAll();
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
            productService.retrieveById(2);
            System.out.println(productService.retrieveById(2));
            System.out.println("---------------------------");

            //Update service test
            System.out.println("Update 2nd product Quantity and Fragility:");
            ProductDto p2 = ProductDto.builder()
                    .title("Ziepes")
                    .description("Ziepju apraksts")
                    .quantity(444)
                    .category(ProductCategory.COSMETICS.getPublicName())
                    .fragility(ProductFragility.FRAGILE.getPublicName())
                    .build();
            productService.updateById(2, p2);
            System.out.println(productService.retrieveById(2));
            System.out.println("---------------------------");

            //Delete service test
//            System.out.println("Delete 2nd product:");
//            crudService.deleteById(2);
//            System.out.println("---------------------------");
            System.out.println("Final products after filter and CRUD tests:");
            allProducts = productService.retrieveAll();
            allProducts.forEach(System.out::println);
            System.out.println("===========================");
        };
    }

    @Bean
    public CommandLineRunner testPackageItemDatabaseLinkage(PackageItemService packageItemService) {
        return args -> {
            packageItemService.create(PackageItemDto.builder()
                    .commodityId(1)
                    .declarationId(1)
                    .quantity(11)
                    .netWeight(14)
                    .value(100)
                    .used(true)
                    .build());

            packageItemService.create(PackageItemDto.builder()
                    .commodityId(2)
                    .declarationId(2)
                    .quantity(22)
                    .netWeight(76)
                    .value(1000)
                    .used(false)
                    .build());
            packageItemService.create(PackageItemDto.builder()
                    .commodityId(3)
                    .declarationId(3)
                    .quantity(44)
                    .netWeight(1)
                    .value(1900)
                    .used(false)
                    .build());
            System.out.println("===========================");
            //All Products
            List<PackageItemDto> allPackageItems = packageItemService.retrieveAll();
            System.out.println("All packageItems:");
            allPackageItems.forEach(System.out::println);
            System.out.println("---------------------------");

        };
    }

}
