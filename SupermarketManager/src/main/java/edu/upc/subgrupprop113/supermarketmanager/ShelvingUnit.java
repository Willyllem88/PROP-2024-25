package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private int uid;

    /**
     * The type of shelving unit, depending on its temperature, may be FROZEN,
     * REFRIGERATED, AMBIENT
     */
    private ProductTemperature temperature;

    /**
     * A list of the products contained by the shelving unit, the product on the floor
     * will be indexed by 0, its height will not change, and set when creating a
     * shelving unit.
     */
    private final List<Product> products;

    @JsonCreator
    public ShelvingUnit(
            @JsonProperty("uid") int uid,
            @JsonProperty("height") int height,
            @JsonProperty("temperature") String temperatureStr,
            @JsonProperty("products") List<String> productNames) {

        this.uid = uid;
        this.temperature = ProductTemperature.valueOf(temperatureStr);

        // Products with their identifier and nothing else
        this.products = new ArrayList<>(Collections.nCopies(height, null));
        for (int i = 0; i < height; ++i) {
            Product product = new Product();
            String productName = productNames.get(i);

            if (productName != null) {
                product.setName(productName);
                this.products.set(i, product);
            }
        }
    }

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
        this.temperature = temperature;
        this.products = new ArrayList<>(Collections.nCopies(height, null));
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
     * Returns an immutable view of the list of products in this shelving unit.
     * The list will always have a size equal to the specified height of the unit.
     *
     * @return an unmodifiable list of products in this shelving unit.
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Sets the list of products in this shelving unit. The product list
     * must match the unit's height in size; otherwise, an exception will be thrown.
     *
     * @param products the list of products to set in this shelving unit.
     * @throws IllegalArgumentException if the provided list's size does not match the unit's height.
     */
    public void setProducts(List<Product> products) {
        if (products.size() != this.products.size()) {
            throw new IllegalArgumentException("The size of the product list must match the unit's height.");
        }

        products.clear();
        this.products.addAll(products);
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
     * Sets a new temperature category for the shelving unit.
     *
     * @param newTemperature the new temperature category for the shelving unit.
     */
    public void setTemperature(ProductTemperature newTemperature) {
        this.temperature = newTemperature;
    }

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
