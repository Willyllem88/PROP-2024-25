package edu.upc.subgrupprop113.supermarketmanager;

import java.util.List;
import java.util.ArrayList;

public class ShelvingUnit {
    private int uid;

    // Product on the top will be indexed by 0
    private List<Product> products;

    ShelvingUnit(int uid, int height) {
        this.uid = uid;
        this.products = new ArrayList<>();

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
}
