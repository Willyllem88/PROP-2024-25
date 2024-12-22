package edu.upc.subgrupprop113.supermarketmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.upc.subgrupprop113.supermarketmanager.utils.RelatedProductSerializer;

import java.util.*;

/**
 * Represents a product with attributes such as name, price, temperature,
 * image path, keywords, and related products.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Product {
    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    private float price;

    /**
     * The temperature category of the product can be: frozen, refrigerated and ambient.
     */
    private ProductTemperature temperature;

    /**
     * The file path of the image that represents the product.
     */
    private String imgPath;

    /**
     * A list of keywords associated with the product, useful for searches.
     */
    private final List<String> keyWords;

    /**
     * A list of how this product is related to all the other products.
     */
    @JsonSerialize(contentUsing = RelatedProductSerializer.class)
    private final List<RelatedProduct> relatedProducts;

    public Product() {
        keyWords = new ArrayList<>();
        relatedProducts = new ArrayList<>();
    }

    /**
     * Creates a product with their main attributes
     *
     * @param name the name of the product
     * @param price the price of the product
     * @param temperature the temperature of the product
     * @param imgPath the image path of the product
     */
    public Product(String name, float price, ProductTemperature temperature, String imgPath) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPath = imgPath;
        this.keyWords = new ArrayList<>();
        this.relatedProducts = new ArrayList<>();
    }

    /**
     * Gets the name of the product
     *
     * @return the name string
     */
    public String getName() { return name; }

    /**
     * Gets the price of the product
     *
     * @return the price as a float
     */
    public float getPrice() { return price; }

    /**
     * Gets the temperature of the product
     *
     * @return the temperature as a ProductTemperature enum
     */
    public ProductTemperature getTemperature() { return temperature; }

    /**
     * Gets the image path of the product
     *
     * @return the image path as a string
     */
    public String getImgPath() { return imgPath; }

    /**
     * Get all the keywords of the product
     *
     * @return an unmodifiable list of product's keywords
     */
    public List<String> getKeyWords() {
        return Collections.unmodifiableList(keyWords);
    }

    /**
     * Gets all the relations of the product
     *
     * @return an unmodifiable list of all the relations
     */
    public List<RelatedProduct> getRelatedProducts() {
        return Collections.unmodifiableList(relatedProducts);
    }

    /**
     * Gets the relation value between this and other products
     *
     * @param other must be a product included in the Catalog
     * @return the related value of the specified products
     * @throws IllegalArgumentException if the product other is not related with the product this
     */
    public float getRelatedValue(Product other) {
        if (this == other) return 1.0f;

        for (RelatedProduct relatedProduct : relatedProducts) {
            if (relatedProduct.getOtherProduct(this) == other) {
                return relatedProduct.getValue();
            }
        }
        throw new IllegalArgumentException("Product not found in related products");
    }

    /**
     * Checks if this product is related to the specified product.
     *
     * @param other must be a product included in the Catalog
     * @return true if this product is related to the specified product, false otherwise
     */
    public Boolean isRelatedTo(Product other) {
        for (RelatedProduct relatedProduct : relatedProducts) {
            if (relatedProduct.getOtherProduct(this) == other) {
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the name of the product
     *
     * @param name the new name for the product
     */
    public void setName(String name) { this.name = name; }

    /**
     * Changes the price of the product
     *
     * @param price the new price value as a float
     */
    public void setPrice(float price) { this.price = price; }

    /**
     * Changes the temperature of the product
     *
     * @param temperature the new temperature, as a ProductTemperature enum
     */
    public void setTemperature(ProductTemperature temperature) { this.temperature = temperature; }

    /**
     * Changes the image path of the product
     *
     * @param imgPath new path to the image
     */
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    /**
     * Sets the list of keywords by clearing the existing list and adding all elements from the provided list.
     *
     * @param keyWords The list of keywords to set.
     */
    public void setKeyWords(List<String> keyWords) {
        this.keyWords.clear();
        this.keyWords.addAll(keyWords);
    }

    /**
     * Adds a keyword to the product
     *
     * @param keyWord to be added to the keyWords list
     */
    public void addKeyWord(String keyWord) {
        this.keyWords.add(keyWord);
    }

    /**
     * Removes a keyword to the product
     *
     * @param keyWord to be removed from the keyWords list
     * @throws IllegalArgumentException if the keyWord is not a contained in the product
     */
    public void eraseKeyWord(String keyWord) {
        if (!this.keyWords.contains(keyWord)) {
            throw new IllegalArgumentException("KeyWord not found in related products");
        }

        this.keyWords.remove(keyWord);
    }

    /**
     * Removes all the keyWords of the product
     */
    public void clearKeyWords() {
        this.keyWords.clear();
    }

    /**
     * Adds a related product to the list of related products for this product.
     *
     * **WARNING**: This method should NOT be called directly outside the `RelatedProduct` class.
     * The `RelatedProduct` constructor already invokes this function automatically when creating
     * relationships between products. Calling this method manually may result in inconsistent
     * data or duplicate relationships.
     *
     * Only call this method if you are certain it won't interfere with the existing relationship
     * logic and data integrity.
     *
     * @param relatedProduct The related product to be added. Must not be null.
     *
     * @throws NullPointerException if the relatedProduct is null.
     * @throws IllegalArgumentException if the related product is already in the list.
     * @throws IllegalArgumentException if this product is already related with the other one.
     */
    public void addRelatedProduct(RelatedProduct relatedProduct) {
        if (relatedProduct == null) {
            throw new NullPointerException("Related product must not be null");
        }

        if (relatedProducts.contains(relatedProduct)) {
            throw new IllegalArgumentException("Related product already exists");
        }

        //Checks is this is already related with the other product
        if (this.isRelatedTo(relatedProduct.getOtherProduct(this))) {
            throw new IllegalArgumentException("Products are already related");
        }

        this.relatedProducts.add(relatedProduct);
    }

    /**
     * Modifies the relation value between this product and another specified product.
     *
     * @param other the product to which the relation value will be modified.
     * @param newValue the new value to set for the relation between the two products.
     * @throws NullPointerException if other is null.
     */
    public void setRelatedValue(Product other, float newValue) {
        if (other == null) {
            throw new NullPointerException("Related product cannot be null");
        }

        for (RelatedProduct relatedProduct : relatedProducts) {
            if (relatedProduct.getOtherProduct(this) == other) {
                relatedProduct.setValue(newValue);
                break;
            }
        }
    }

    /**
     * Removes all relationships with other products for the current product.
     *
     * This method clears all related products and invokes the `eraseRelation` method
     * on each of the related products to ensure that the relationship is eliminated
     * on both sides.
     *
     * **WARNING**: This operation is destructive and should only be called if you are
     * certain that removing all product relationships is necessary. It may have
     * unintended side effects, such as breaking the integrity of the product catalog
     * or causing inconsistency in related product data. Use with extreme caution!
     */
    public void eliminateAllRelations() {
        for (RelatedProduct relatedProduct : relatedProducts) {

            Product otherProduct = relatedProduct.getOtherProduct(this);
            otherProduct.eraseRelation(this);
        }
        relatedProducts.clear();
    }

    /**
     * Returns a string with the product information
     *
     * @return a string with the product information
     */
    @JsonIgnore
    public String getInfo() {
        String res = "";
        res += "Name: " + name + "\n";
        res += "Price: " + price + "€\n";
        res += "Temperature: " + temperature + "\n";
        res += "Image path: " + imgPath + "\n";
        res += "KeyWords: " + keyWords + "\n";
        res += "Related products:\n";
        for (RelatedProduct relatedProduct : relatedProducts) {
            res += "\t" + relatedProduct.getOtherProduct(this).getName() + ": " + relatedProduct.getValue() + "\n";
        }
        return res;
    }


    /**
     * Removes the relation between this and other products
     *
     * **WARNING**: This operation is destructive and should only be called if you are
     * certain that removing this product relationships is necessary. It may have
     * unintended side effects, such as breaking the integrity of the product catalog
     * or causing inconsistency in related product data. Use with extreme caution!
     *
     * @param other the other product that this product will no longer be related to
     */
    private void eraseRelation(Product other) {
        relatedProducts.removeIf(relatedProduct -> relatedProduct.getOtherProduct(this) == other);
    }
}
