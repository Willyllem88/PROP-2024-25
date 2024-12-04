package edu.upc.subgrupprop113.supermarketmanager.dtos;

import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;

/**
 * Data Transfer Object (DTO) representing {@link RelatedProduct}.
 * <p>
 * This class captures the strength of the relationship between two products and identifies the related products
 * by their names.
 * </p>
 */
public class RelatedProductDto {

    /**
     * The strength of the relationship between the two products.
     * <p>
     * Represented as a float, a value between 0.0 and 1.0, where higher values indicate stronger relationships.
     * </p>
     */
    private float value;

    /**
     * The name of the first product in the relationship.
     */
    private String product1;

    /**
     * The name of the second product in the relationship.
     */
    private String product2;

    /**
     * Default constructor for creating an empty `RelatedProductDto` instance.
     */
    public RelatedProductDto() {
    }

    /**
     * Constructs a `RelatedProductDto` with the specified relationship value and product names.
     *
     * @param value    the strength of the relationship between the two products.
     * @param product1 the name of the first product in the relationship.
     * @param product2 the name of the second product in the relationship.
     */
    public RelatedProductDto(float value, String product1, String product2) {
        this.value = value;
        this.product1 = product1;
        this.product2 = product2;
    }

    /**
     * Gets the strength of the relationship.
     *
     * @return the relationship value.
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the strength of the relationship.
     *
     * @param value the relationship value to set.
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Gets the name of the first product in the relationship.
     *
     * @return the name of the first product.
     */
    public String getProduct1() {
        return product1;
    }

    /**
     * Sets the name of the first product in the relationship.
     *
     * @param product1 the name of the first product to set.
     */
    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    /**
     * Gets the name of the second product in the relationship.
     *
     * @return the name of the second product.
     */
    public String getProduct2() {
        return product2;
    }

    /**
     * Sets the name of the second product in the relationship.
     *
     * @param product2 the name of the second product to set.
     */
    public void setProduct2(String product2) {
        this.product2 = product2;
    }
}
