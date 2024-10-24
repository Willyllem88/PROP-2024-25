package edu.upc.subgrupprop113.supermarketmanager;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the relation between two values
 */
public class RelatedProduct {
    /**
     * The relation value between those products, must be between 0 and 1, both included
     */
    float value;

    /**
     * The two products that are related, they must be different and not null
     */
    Product product1, product2;

    /**
     * Constructs a RelatedProduct instance with the specified products and value.
     *
     * @param product1 the first product to be related
     * @param product2 the second product to be related
     * @param value    the value associated with the relationship, must be between 0.0 and 1.0 (inclusive)
     * @throws IllegalArgumentException if either product is null
     * @throws IllegalArgumentException both products are the same
     * @throws IllegalArgumentException value out of bounds
     */
    public RelatedProduct(Product product1, Product product2, float value) {
        if (product1 == null || product2 == null) {
            throw new IllegalArgumentException("Neither product1 nor product2 are null");
        }
        if (product1 == product2) {
            throw new IllegalArgumentException("Product 1 and Product 2 are the same");
        }
        if (value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("Value must be a float between 0 and 1.0, both included");
        }

        this.product1 = product1;
        this.product2 = product2;
        this.value = value;

        product1.addRelatedProduct(this);
        product2.addRelatedProduct(this);
    }

    /**
     * Gets the value of the relation
     *
     * @return the value of the relation of this
     */
    float getValue() { return value; }

    /**
     * Returns a list of the two related products.
     *
     * @return a List containing product1 and product2.
     */
    List<Product> getProducts() {
        return Arrays.asList(product1, product2);
    }

    /**
     * Gets the product with whom Product p is related
     *
     * @param product the product which the result is related with
     * @return the product that is related with the argument product
     * @throws IllegalArgumentException if product is not contained in this class, and should be
     */
    Product getOtherProduct(Product product) {
        if (product == product1) return product2;
        else if (product == product2) return product1;
        throw new IllegalArgumentException("Product p is not contained in this class");
    }

    /**
     * Checks if the given product is one of the related products.
     *
     * @param product the product to check for relation
     * @return true if the product is either product1 or product2; false otherwise
     */
    public Boolean contains(Product product) {
        return product == product1 || product == product2;
    }

    /**
     * Sets the value associated with this related product.
     *
     * @param value the new value to set for the related products
     * @throws IllegalArgumentException if the value is not between 0.0 and 1.0 (inclusive)
     */
    void setValue(float value) {
        if (value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("Value must be a float between 0 and 1.0, both included");
        }

        this.value = value;
    }
}
