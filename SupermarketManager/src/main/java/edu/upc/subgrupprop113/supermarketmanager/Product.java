package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private float price;
    private ProductTemperature temperature;
    private String imgPath;
    private List<String> keyWords;

    public Product(String name, float price, ProductTemperature temperature, String imgPath) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPath = imgPath;
        this.keyWords = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public ProductTemperature getTemperature() { return temperature; }

    public String getImgPath() {
        return imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setTemperature(ProductTemperature temperature) { this.temperature = temperature; }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
