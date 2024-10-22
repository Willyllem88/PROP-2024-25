package edu.upc.subgrupprop113.supermarketmanager;

import java.util.List;
import java.util.ArrayList;

public class ShelvingUnit {
    private int uid;
    private List<Product> products; // Product on the floor will be indexed by 0
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

    public int getHeight() { return products.size(); }

    public void setTemperature(ProductTemperature newTemperature) { this.temperature = newTemperature; }

    /**
     * Puts a product in the shelving unit, at the height specified by height
     *
     * @param product the product to be inserted
     * @param height where the product will be placed, being 0 the bottom of the shelving unit
     * @throws NullPointerException if product is null
     * @throws IndexOutOfBoundsException if height is out of range
     */
    public void addProduct(Product product, int height) {
        if (product == null) {
            throw new NullPointerException("Product cannot be null.");
        }
        if (height < 0 || height >= products.size()) {
            throw new IndexOutOfBoundsException("Invalid height: " + height);
        }

        products.set(height, product);
    }

    /**
     * Removes the product at the specified height from the shelving unit.
     *
     * @param height the position where the product will be removed, with 0 being the bottom of the shelving unit.
     * @throws IndexOutOfBoundsException if height is out of range.
     */
    public void removeProduct(int height) {
        if (height < 0 || height >= products.size()) {
            throw new IndexOutOfBoundsException("Invalid height: " + height);
        }
        products.set(height, null);
    }

    /**
     * Empties all the shelves of the shelving unit
     */
    public void emptyShelvingUnit() {
        products.replaceAll(ignored -> null);
    }
}
