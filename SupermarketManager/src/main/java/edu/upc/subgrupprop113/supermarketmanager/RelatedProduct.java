package edu.upc.subgrupprop113.supermarketmanager;

public class RelatedProduct {
    float value;
    Product product1, product2;

    RelatedProduct(Product product1, Product product2, float value) {
        this.product1 = product1;
        this.product2 = product2;
        this.value = value;
    }

    float getValue() { return value; }

    //TODO: implement this function, must return a list of the two related objects.
    //List<Product> getProducts() {return new }

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

    void setValue(float value) { this.value = value; }

}
