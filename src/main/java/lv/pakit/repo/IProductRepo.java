package lv.pakit.repo;

import lv.pakit.model.ProductFragility;
import lv.pakit.model.ProductCategory;
import lv.pakit.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface IProductRepo extends JpaRepository<Product, Integer> {

    Product findByTitleAndDescriptionAndQuantityAndCategoryAndFragility(String title, String description, int quantity, ProductCategory category, ProductFragility fragility);

    ArrayList<Product> findByQuantityLessThan(int threshold) throws Exception;

    ArrayList<Product> findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(String phrase, String phrase2) throws Exception;

    ArrayList<Product> findByCategory(ProductCategory category);

    ArrayList<Product> findByFragility(ProductFragility fragility);

}
