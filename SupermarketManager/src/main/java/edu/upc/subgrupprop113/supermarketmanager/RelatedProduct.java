package edu.upc.subgrupprop113.supermarketmanager;

import java.util.Arrays;
import java.util.List;

public class RelatedProduct {
    float value;
    Product product1, product2;

    public RelatedProduct(Product product1, Product product2, float value) {
        if (product1 == null || product2 == null) {
            throw new IllegalArgumentException("Neither product1 nor product2 are null");
        }

        this.product1 = product1;
        this.product2 = product2;
        this.value = value;

        product1.addRelatedProduct(this);
        product2.addRelatedProduct(this);
    }

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
     */
    void setValue(float value) { this.value = value; }
}
