package lv.pakit.service;

import lv.pakit.model.Product;

import java.util.ArrayList;

public interface IProductCRUDService {

    public abstract void create(Product product);

    public abstract Product retriveById(int id) throws Exception;

    public abstract ArrayList<Product> retriveAll() throws Exception;

    public abstract void updateById(int id, Product product) throws Exception;

    public abstract void deleteById(int id) throws Exception;
}
