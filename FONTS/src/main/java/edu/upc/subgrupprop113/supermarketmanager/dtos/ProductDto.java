package edu.upc.subgrupprop113.supermarketmanager.dtos;

import java.util.List;

/**
 * A Data Transfer Object (DTO) representing a product in the supermarket system.
 * <p>
 * This class encapsulates all the necessary details about a product, such as its name,
 * price, temperature category, image file path, and a list of associated keywords.
 * It is used to transfer data between different layers of the application.
 * </p>
 */
public class ProductDto {

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    private float price;

    /**
     * The temperature category of the product (e.g., frozen, refrigerated, ambient).
     */
    private String temperature;

    /**
     * The file path of the image representing the product.
     */
    private String imgPath;

    /**
     * A list of keywords associated with the product, useful for searches.
     */
    private List<String> keyWords;

    /**
     * Default constructor for creating an empty ProductDto instance.
     */
    public ProductDto() {
    }

    /**
     * Constructs a ProductDto with the specified details.
     *
     * @param name        the name of the product.
     * @param price       the price of the product.
     * @param temperature the temperature category of the product.
     * @param imgPath     the file path of the product image.
     * @param keyWords    the list of keywords associated with the product.
     */
    public ProductDto(String name, float price, String temperature, String imgPath, List<String> keyWords) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPath = imgPath;
        this.keyWords = keyWords;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the product.
     *
     * @return the product price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Returns the temperature category of the product.
     *
     * @return the product temperature category.
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Returns the file path of the product image.
     *
     * @return the product image file path.
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * Returns the list of keywords associated with the product.
     *
     * @return the product keywords.
     */
    public List<String> getKeyWords() {
        return keyWords;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the new product name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the new product price.
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Sets the temperature category of the product.
     *
     * @param temperature the new product temperature category.
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Sets the file path of the product image.
     *
     * @param imgPath the new product image file path.
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * Sets the list of keywords associated with the product.
     *
     * @param keyWords the new list of product keywords.
     */
    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}
