package edu.upc.subgrupprop113.supermarketmanager;

import java.util.List;
import java.util.ArrayList;

public class ShelvingUnit {
    private int uid;
    private List<Product> products; // Product on the top will be indexed by 0
    private ProductTemperature temperature;

    ShelvingUnit(int uid, int height, ProductTemperature temperature) {
        this.uid = uid;
        this.products = new ArrayList<>();
        this.temperature = temperature;

        for (int i = 0; i < height; i++) {
            products.add(null);
        }
    }

    public int getUid() {
        return uid;
    }

    public Product getProduct(int index) {
        return products.get(index);
    }

    public ProductTemperature getTemperature() { return temperature; }

    public void setTemperature(ProductTemperature temperature) { this.temperature = temperature; }
}
