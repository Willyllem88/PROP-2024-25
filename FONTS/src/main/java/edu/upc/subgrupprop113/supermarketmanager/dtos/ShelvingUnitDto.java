package edu.upc.subgrupprop113.supermarketmanager.dtos;

import java.util.List;

/**
 * A Data Transfer Object (DTO) representing a shelving unit in the supermarket system.
 * <p>
 * This class encapsulates all necessary details about a shelving unit, such as its unique identifier (UID),
 * temperature category, and the list of products it contains.
 * It is used to transfer data between different layers of the application.
 * </p>
 */
public class ShelvingUnitDto {

    /**
     * The unique identifier (UID) of the shelving unit.
     */
    private int uid;

    /**
     * The temperature type of the shelving unit.
     * <p>Possible values are:</p>
     * <ul>
     * <li>FROZEN</li>
     * <li>REFRIGERATED</li>
     * <li>AMBIENT</li>
     * </ul>
     */
    private String temperature;

    /**
     * A list of products contained by the shelving unit.
     * <p>
     * The product at the bottom (floor) of the shelving unit is indexed at 0.
     * The height of the shelving unit is fixed when it is created.
     * </p>
     */
    private List<ProductDto> products;

    /**
     * Constructs a new `ShelvingUnitDto` with the specified details.
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
     * Constructs a new empty `ShelvingUnitDto`.
     */
    public ShelvingUnitDto() {
    }

    /**
     * Returns the unique identifier (UID) of the shelving unit.
     *
     * @return the shelving unit's UID.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the unique identifier (UID) of the shelving unit.
     *
     * @param uid the new UID of the shelving unit.
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Returns the temperature category of the shelving unit.
     *
     * @return the temperature type of the shelving unit.
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature category of the shelving unit.
     *
     * @param temperature the new temperature type of the shelving unit.
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Returns the list of products contained in the shelving unit.
     *
     * @return the list of products in the shelving unit.
     */
    public List<ProductDto> getProducts() {
        return products;
    }

    /**
     * Sets the list of products for the shelving unit.
     *
     * @param products the new list of products to be contained in the shelving unit.
     */
    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
