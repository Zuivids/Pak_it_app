package lv.pakit.service;

import java.util.ArrayList;

import lv.pakit.model.Product;

public interface IProductFilteringService {

    public abstract ArrayList<Product> filterByPrice(float threshold);

    public abstract ArrayList<Product> filterByQuantityLess(int threshold);

    public abstract ArrayList<Product> filterByTitleOrDescription(String phrase);

    public abstract float calculateTotalValueOfProducts();


}