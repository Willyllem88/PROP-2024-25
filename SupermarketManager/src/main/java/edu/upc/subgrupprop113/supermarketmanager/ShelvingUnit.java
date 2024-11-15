package edu.upc.subgrupprop113.supermarketmanager;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Represent a shelving unit, this will contain different shelves, each one with a
 * different product
 */
public class ShelvingUnit {
    /**
     * The uid of the shelving unit
     */
    private final int uid;

    /**
     * A list of the products contained by the shelving unit, the product on the floor
     * will be indexed by 0, its height will not change, and set when creating a
     * shelving unit.
     */
    private final List<Product> products;

    /**
     * The type of shelving unit, depending on its temperature, may be FROZEN,
     * REFRIGERATED, AMBIENT
     */
    private ProductTemperature temperature;

    /**
     * Creates a new shelving unit with a specified unique identifier, height (number of product slots),
     * and temperature category. The shelving unit is initialized with empty slots (null) for products.
     *
     * @param uid the unique identifier for the shelving unit.
     * @param height the number of slots (or shelves) in the shelving unit.
     * @param temperature the temperature category for storing products (e.g., frozen, refrigerated).
     */
    public ShelvingUnit(int uid, int height, ProductTemperature temperature) {
        this.uid = uid;
        this.products = new ArrayList<>();
        this.temperature = temperature;

        for (int i = 0; i < height; i++) {
            products.add(null);
        }
    }
    /**
     * Returns the unique identifier of the shelving unit.
     *
     * @return the unique identifier (uid) of the shelving unit.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Retrieves the product at the specified index.
     *
     * @param index the index of the product slot in the shelving unit.
     * @return the product at the specified index, or null if the slot is empty.
     */
    public Product getProduct(int index) {
        return products.get(index);
    }

    /**
     * Returns a list of all the products in the shelving unit.
     *
     * @return an unmodifiable list of all the products in the shelving unit.
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Returns the temperature category of the shelving unit.
     *
     * @return the temperature category of the shelving unit (e.g., frozen, refrigerated).
     */
    public ProductTemperature getTemperature() {
        return temperature;
    }

    /**
     * Returns the height of the shelving unit, defined as the number of product slots.
     *
     * @return the number of product slots in the shelving unit.
     */
    public int getHeight() {
        return products.size();
    }

    /**
     * Returns whether the shelving unit is empty (i.e., all slots are empty).
     *
     * @return true if all slots are empty, false otherwise.
     */
    public Boolean isEmpty() {
        for (Product product : products) {
            if (product != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets a new temperature category for the shelving unit.
     *
     * @param newTemperature the new temperature category for the shelving unit.
     * @throws IllegalStateException if in the shelving unit were any products cause the new temperature won't be compatible.
     */
    public void setTemperature(ProductTemperature newTemperature) {
        for (Product product : products) {
            if (product != null) {
                throw new IllegalStateException("The temperature of the product is not compatible with the shelving unit.");
            }
        }
        this.temperature = newTemperature;
    }

    /**
     * Puts a product in the shelving unit, at the height specified by height
     *
     * @param product the product to be inserted
     * @param height where the product will be placed, being 0 the bottom of the shelving unit
     * @throws NullPointerException if product is null
     * @throws IndexOutOfBoundsException if height is out of range
     * @throws  IllegalStateException if the product temperature is not the same as the shelving unit's temperature.
     */
    public void addProduct(Product product, int height) {
        if (product == null) {
            throw new NullPointerException("Product cannot be null.");
        }
        if (height < 0 || height >= products.size()) {
            throw new IndexOutOfBoundsException("Invalid height: " + height);
        }
        if (product.getTemperature() != this.temperature) {
            throw new IllegalStateException("The temperature of the product is not compatible with the shelving unit.");
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

    /**
     * Returns a string representation of the shelving unit. Including the information of its products.
     *
     * @return a string representation of the shelving unit.
     */
    public String getInfo() {
        String res = "";
        res += "----- Shelving Unit Information -----\n";
        res += "UID: " + uid + "\n";
        res += "Shelving unit temperature: " + temperature + "\n";
        res += "Shelving unit size: " + products.size() + "\n";
        res += "-------------------------------\n";
        for (Product product : products) {
            if (product != null) res += product.getInfo();
            else {
                res += "\n";
                res += "- EMPTY SHELF -\n";
                res += "\n";
            }
            res += "-------------------------------\n";
        }
        return res;
    }

}
