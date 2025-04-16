package lv.pakit.service;

import lv.pakit.model.Product;

import java.util.ArrayList;

public interface IProductCRUDService {

    public abstract void create(Product product);

    public abstract Product retriveById(int id);

    public abstract ArrayList<Product> retriveAll();

    public abstract void updateById(int id, Product product);

    public abstract void deleteById(int id);
}
