package edu.upc.subgrupprop113.supermarketmanager.dtos;

import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a {@link ShelvingUnit}.
 * <p>
 * This class encapsulates and transfers data related to a shelving unit,
 * including its unique identifier, temperature category, and the list of products it contains.
 * </p>
 */
public class ShelvingUnitDto {

    /**
     * The unique identifier of the shelving unit.
     */
    private int uid;

    /**
     * The temperature category of the shelving unit.
     * <p>
     * Can be one of the following: "FROZEN", "REFRIGERATED", or "AMBIENT".
     * </p>
     */
    private String temperature;

    /**
     * A list of products contained in the shelving unit.
     */
    private List<ProductDto> products;

    /**
     * Default constructor for creating an empty `ShelvingUnitDto` instance.
     */
    public ShelvingUnitDto() {
    }

    /**
     * Constructs a `ShelvingUnitDto` with the specified attributes.
     *
     * @param uid         the unique identifier of the shelving unit.
     * @param temperature the temperature category of the shelving unit.
     * @param products    the list of products contained in the shelving unit.
     */
    public ShelvingUnitDto(int uid, String temperature, List<ProductDto> products) {
        this.uid = uid;
        this.temperature = temperature;
        this.products = products;
    }

    /**
     * Gets the unique identifier of the shelving unit.
     *
     * @return the shelving unit's unique identifier.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the unique identifier of the shelving unit.
     *
     * @param uid the unique identifier to set.
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Gets the temperature category of the shelving unit.
     *
     * @return the temperature category of the shelving unit.
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature category of the shelving unit.
     *
     * @param temperature the temperature category to set.
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the list of products contained in the shelving unit.
     *
     * @return the list of products in the shelving unit.
     */
    public List<ProductDto> getProducts() {
        return products;
    }

    /**
     * Sets the list of products contained in the shelving unit.
     *
     * @param products the list of products to set.
     */
    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}

