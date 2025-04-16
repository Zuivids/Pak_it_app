package lv.pakit.service;

import java.util.ArrayList;

import lv.pakit.model.Product;

public interface IProductFilteringService {

    public abstract ArrayList<Product> filterByQuantityLess(int threshold) throws Exception;

    public abstract ArrayList<Product> filterByTitleOrDescription(String phrase) throws Exception;


}