package edu.upc.subgrupprop113.supermarketmanager;

public class Product {
    private String name;
    private float price;
    private String imgPath;

    public Product(String name, float price, String imgPath) {
        this.name = name;
        this.price = price;
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
