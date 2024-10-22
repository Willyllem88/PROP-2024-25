package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private float price;
    private ProductTemperature temperature;
    private String imgPath;
    private List<String> keyWords;
    private List<RelatedProduct> relatedProducts;

    public Product(String name, float price, ProductTemperature temperature, String imgPath) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPath = imgPath;
        this.keyWords = new ArrayList<String>();
        this.relatedProducts = new ArrayList<>();
    }

    public String getName() { return name; }

    public float getPrice() { return price; }

    public ProductTemperature getTemperature() { return temperature; }

    public String getImgPath() { return imgPath; }

    /**
     * Get all the keywords of the product
     *
     * @return a copy of the list of keyWords
     */
    public List<String> getKeyWords() {
        return new ArrayList<String>(keyWords);
    }

    /**
     * Gets the relation value between this and other products
     *
     * @param other must be a product included in the Catalog
     * @return the related value of the specified products
     * @throws IllegalArgumentException if the product other is not related with the product this
     */
    public float getRelatedValue(Product other) {
        for (RelatedProduct relatedProduct : relatedProducts) {
            if (relatedProduct.getOtherProduct(this) == other) {
                return relatedProduct.getValue();
            }
        }
        throw new IllegalArgumentException("Product not found in related products");
    }

    public void setName(String name) { this.name = name; }

    public void setPrice(float price) { this.price = price; }

    public void setTemperature(ProductTemperature temperature) { this.temperature = temperature; }

    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    public void addKeyWord(String keyWord) {
        this.keyWords.add(keyWord);
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
     * @throws IllegalArgumentException if the related product is already in the list.
     * @throws NullPointerException if the relatedProduct is null.
     */
    void addRelatedProduct(RelatedProduct relatedProduct) {
        if (relatedProduct == null) {
            throw new NullPointerException("Related product must not be null");
        }

        if (relatedProducts.contains(relatedProduct)) {
            throw new IllegalArgumentException("Related product already exists");
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

    private void eraseRelation(Product other) {
        relatedProducts.removeIf(relatedProduct -> relatedProduct.getOtherProduct(this) == other);
    }
}
