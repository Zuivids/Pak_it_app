package lv.pakit.service;

import java.util.ArrayList;
import java.util.List;

import lv.pakit.dto.ProductDto;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.ProductFragility;
import lv.pakit.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;

@Service
public class ProductService {

    @Autowired
    private IProductRepo productRepo;

    public ArrayList<Product> filterByQuantityLess(int threshold) throws Exception {
        if (threshold <= 0) throw new Exception("Threshold smaller than 0");
        if (productRepo.count() == 0) throw new Exception("There is no product in system!");
        return productRepo.findByQuantityLessThan(threshold);
    }

    public ArrayList<Product> filterByTitleOrDescription(String phrase) throws Exception {
        if (phrase == null) throw new Exception("Phrase is with null address");
        if (productRepo.count() == 0) throw new Exception("There is no product in system!");
        return productRepo.findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(phrase, phrase);
    }

    public void create(ProductDto productDto) {
        Product product = mapToProduct(productDto);

        Product existedProduct = productRepo.findByTitleAndDescriptionAndQuantityAndCategoryAndFragility(
                product.getTitle(),
                product.getDescription(),
                product.getQuantity(),
                product.getCategory(),
                product.getFragility()
        );

        if (existedProduct != null) {
            existedProduct.setQuantity(existedProduct.getQuantity() + product.getQuantity());
            productRepo.save(existedProduct);
        } else {
            productRepo.save(product);
        }
    }

    public ProductDto retrieveById(int id) {
        Product product = requireProductById(id);
        return mapToProductDto(product);
    }

    public List<ProductDto> retrieveAll() {
        return productRepo.findAll().stream()
                .map(this::mapToProductDto)
                .toList();
    }

    public void updateById(int id, ProductDto productDto) {
        Product productForUpdating = requireProductById(id);
        Product product = mapToProduct(retrieveById(id));

        productForUpdating.setTitle(product.getTitle());
        productForUpdating.setDescription(product.getDescription());
        productForUpdating.setQuantity(product.getQuantity());
        productForUpdating.setCategory(product.getCategory());
        productForUpdating.setFragility(product.getFragility());
        productRepo.save(productForUpdating);
    }

    public void deleteById(int id) {
        Product product = requireProductById(id);
        productRepo.deleteById(id);
    }

    private ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .category(product.getCategory().getPublicName())
                .fragility(product.getFragility().getPublicName())
                .build();
    }

    private Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .title(productDto.getTitle())
                .quantity(productDto.getQuantity())
                .description(productDto.getDescription())
                .category(ProductCategory.fromName(productDto.getCategory()))
                .fragility(ProductFragility.fromName(productDto.getFragility()))
                .build();
    }

    private Product requireProductById(int id) {
        return productRepo.findById(id).orElseThrow(() -> new NotFoundException("Product with (" + id + ") is not in system!"));
    }
}
