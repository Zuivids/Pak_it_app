package lv.pakit.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;

@Service
public class ProductService {

    @Autowired
    private IProductRepo productRepo;

    public ArrayList<Product> filterByQuantityLess(int threshold) throws Exception {
        if(threshold <= 0) throw new Exception("Threshold smaller than 0");
        if(productRepo.count() == 0 ) throw new Exception("There is no product in system!");
        return productRepo.findByQuantityLessThan(threshold);
    }

    public ArrayList<Product> filterByTitleOrDescription(String phrase) throws Exception {
        if(phrase == null) throw new Exception("Phrase is with null address");
        if(productRepo.count() == 0 ) throw new Exception("There is no product in system!");
        return productRepo.findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(phrase, phrase);
    }

    public void create(Product product) {
        Product existedProduct = productRepo.findByTitleAndDescriptionAndQuantityAndCategoryAndFragility(
                product.getTitle(),
                product.getDescription(),
                product.getQuantity(),
                product.getCategory(),
                product.getFragility()
        );

        if(existedProduct != null) {
            existedProduct.setQuantity(existedProduct.getQuantity() + product.getQuantity());
            productRepo.save(existedProduct);
        } else {
            productRepo.save(product);
        }
    }

    public Product retriveById(int id) throws Exception {
        if(id < 0) throw new Exception("ID should be positive!");
        return productRepo.findById(id).orElseThrow(() -> new Exception("Product with ("+id+") is not in system!"));
    }

    public ArrayList<Product> retriveAll() throws Exception {
        if(productRepo.count() == 0 ) throw new Exception("Table is empty!");
        return (ArrayList<Product>) productRepo.findAll();
    }

    public void updateById(int id, Product product) throws Exception {
        Product productForUpdating = retriveById(id);
        productForUpdating.setTitle(product.getTitle());
        productForUpdating.setDescription(product.getDescription());
        productForUpdating.setQuantity(product.getQuantity());
        productForUpdating.setCategory(product.getCategory());
        productForUpdating.setFragility(product.getFragility());
        productRepo.save(productForUpdating);
    }

    public void deleteById(int id) throws Exception {
        if(id <= 0) throw new Exception("Invalid ID!");
        productRepo.deleteById(id);
    }
}
