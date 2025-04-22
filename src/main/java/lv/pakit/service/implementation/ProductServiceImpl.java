package lv.pakit.service.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.pakit.service.IProductFilteringService;
import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import lv.pakit.service.IProductCRUDService;

@Service
public class ProductServiceImpl implements IProductCRUDService, IProductFilteringService {

    @Autowired
    private IProductRepo productRepo;

    @Override
    public ArrayList<Product> filterByQuantityLess(int threshold) throws Exception{

        if(threshold <= 0) throw new Exception("Threshold smaller than 0");

        if(productRepo.count() == 0 ) throw new Exception("There is no product in system!");

        return productRepo.findByQuantityLessThan(threshold);

    }

    @Override
    public ArrayList<Product> filterByTitleOrDescription(String phrase) throws Exception{

        if(phrase == null) throw new Exception("Phrase is with null adress");

        if(productRepo.count() == 0 ) throw new Exception("There is no product in system!");

        return productRepo.findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(phrase, phrase);

    }

    @Override
    public void create(Product product) {

        Product existedProduct = productRepo.findByTitleAndDescriptionAndQuantityAndCategoryAndFragility(product.getTitle(),
                product.getDescription(), product.getQuantity(), product.getCategory(), product.getFragility());

        if(existedProduct != null) {
            existedProduct.setQuantity(existedProduct.getQuantity() + product.getQuantity());
            productRepo.save(existedProduct);
            return;
        }

        productRepo.save(product);

    }

    @Override
    public Product retriveById(int id) throws Exception {
        if(id < 0) throw new Exception("ID should be positive!");

        if(productRepo.existsById(id)) {
            return productRepo.findById(id).get();
        }
        else {
            throw new Exception("Product with ("+id+") is not in system!");
        }
    }

    @Override
    public ArrayList<Product> retriveAll() throws Exception {
        if(productRepo.count() == 0 ) throw new Exception("Table is empty!");

        return (ArrayList<Product>) productRepo.findAll();

    }

    @Override
    public void updateById(int id, Product product) throws Exception {
        Product productForUpdating = retriveById(id);

        productForUpdating.setTitle(product.getTitle());
        productForUpdating.setDescription(product.getDescription());
        productForUpdating.setQuantity(product.getQuantity());
        productForUpdating.setCategory(product.getCategory());
        productForUpdating.setFragility(product.getFragility());

        productRepo.save(productForUpdating);

    }

    @Override
    public void deleteById(int id) throws Exception {
        if(id <= 0) throw new Exception("Invalid ID!");

        productRepo.deleteById(id);
    }

}
