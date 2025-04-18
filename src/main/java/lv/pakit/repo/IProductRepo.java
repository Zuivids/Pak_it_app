package lv.pakit.repo;

import lv.pakit.model.Fragility;
import lv.pakit.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface IProductRepo extends CrudRepository<Product, Integer> {

    Product findByTitleAndDescriptionAndQuantityAndTypeAndFragility(String title, String description,  int quantity, String type, Fragility fragility);

    ArrayList<Product> findByQuantityLessThan(int threshold) throws Exception;

    ArrayList<Product> findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(String phrase, String phrase2) throws Exception;

    ArrayList<Product> findByTypeIgnoreCaseLike(String type);

    ArrayList<Product> findByFragility(String fragility);

}
