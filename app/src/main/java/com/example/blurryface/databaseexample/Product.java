package com.example.blurryface.databaseexample;

/**
 * Created by BlurryFace on 9/25/2017.
 */

public class Product {
    private int _id,_quantity;
    private String productName;
    public Product()
    {
        _id = 0;
        _quantity=0;
        productName = "N/A";
    }
    public Product(String name,int quantity)
    {
        productName = name;
        _quantity = quantity;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
