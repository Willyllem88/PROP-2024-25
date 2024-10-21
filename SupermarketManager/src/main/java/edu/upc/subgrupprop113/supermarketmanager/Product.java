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

    public void addRelatedProduct(RelatedProduct relatedProduct) {
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
     * For each related product, it invokes the `eraseRelation` method on the
     * corresponding "other" product, effectively eliminating the relationship.
     */
    public void eliminateAllRelations() {
        for (RelatedProduct relatedProduct : relatedProducts) {

            Product otherProduct = relatedProduct.getOtherProduct(this);
            otherProduct.eraseRelation(this);
        }
        relatedProducts.clear();
    }

    /**
     * Removes the relationship between the current product and the specified product.
     * If the provided product is null, a NullPointerException is thrown.
     *
     * @param other The product to remove the relationship with.
     * @throws NullPointerException if the provided product is null.
     */
    private void eraseRelation(Product other) {
        if (other == null) {
            throw new NullPointerException("Related product cannot be null");
        }
        relatedProducts.removeIf(relatedProduct -> relatedProduct.getOtherProduct(this) == other);
    }
}
