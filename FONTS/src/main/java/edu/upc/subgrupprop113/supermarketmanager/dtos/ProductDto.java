package edu.upc.subgrupprop113.supermarketmanager.dtos;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a {@link Product}
 * <p>
 * This class is used to encapsulate and transfer product data, including its attributes,
 * keywords for categorization, and related products information.
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
     * The temperature category of the product.
     * <p>
     * Can be one of the following: "FROZEN", "REFRIGERATED", or "AMBIENT".
     * </p>
     */
    private String temperature;

    /**
     * The file path to the image representing the product.
     */
    private String imgPath;

    /**
     * A list of keywords associated with the product.
     * <p>
     * These keywords are useful for search and categorization purposes.
     * </p>
     */
    private List<String> keywords;

    /**
     * A list of related products and their relationship details.
     */
    private List<RelatedProductDto> relatedProducts;

    /**
     * Default constructor for creating an empty `ProductDto` instance.
     */
    public ProductDto() {
    }

    /**
     * Constructs a `ProductDto` with the specified attributes.
     *
     * @param name            the name of the product.
     * @param price           the price of the product.
     * @param temperature     the temperature category of the product.
     * @param imgPath         the file path to the product's image.
     * @param keywords        the list of keywords associated with the product.
     * @param relatedProducts the list of related products.
     */
    public ProductDto(String name, float price, String temperature, String imgPath, List<String> keywords, List<RelatedProductDto> relatedProducts) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPath = imgPath;
        this.keywords = keywords;
        this.relatedProducts = relatedProducts;
    }

    /**
     * Gets the name of the product.
     *
     * @return the product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the product price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the product price to set.
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Gets the temperature category of the product.
     *
     * @return the temperature category.
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature category of the product.
     *
     * @param temperature the temperature category to set.
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the file path to the product's image.
     *
     * @return the image file path.
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * Sets the file path to the product's image.
     *
     * @param imgPath the image file path to set.
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * Gets the list of keywords associated with the product.
     *
     * @return the list of keywords.
     */
    public List<String> getKeywords() {
        return keywords;
    }


    /**
     * Sets the keywords to the product.
     *
     * @param keywords a list containing the new keywords.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Gets the list of related products and their relationship details.
     *
     * @return the list of related products.
     */
    public List<RelatedProductDto> getRelatedProducts() {
        return relatedProducts;
    }

    /**
     * Sets the list of related products and their relationship details.
     *
     * @param relatedProducts the list of related products to set.
     */
    public void setRelatedProducts(List<RelatedProductDto> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}

