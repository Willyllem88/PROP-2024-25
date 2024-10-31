package edu.upc.subgrupprop113.supermarketmanager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RelatedProductTest {
    private Product product1;
    private Product product2;
    private RelatedProduct relatedProduct;

    @BeforeEach
    void setUp() {
        // Initialize products with dummy data
        product1 = new Product("Product1", 10.0f, ProductTemperature.FROZEN, "path/to/image1.jpg");
        product2 = new Product("Product2", 20.0f, ProductTemperature.FROZEN, "path/to/image2.jpg");
        relatedProduct = new RelatedProduct(product1, product2, 0.5f);
    }

    @Test
    void testGetValue() {
        assertEquals(0.5f, relatedProduct.getValue(), "The value should be 0.5");
    }

    @Test
    void testGetProducts() {
        List<Product> products = relatedProduct.getProducts();
        assertEquals(2, products.size(), "The list should contain exactly two products");
        assertTrue(products.contains(product1), "The list should contain product1");
        assertTrue(products.contains(product2), "The list should contain product2");
    }

    @Test
    void testGetOtherProductWithProduct1() {
        Product otherProduct = relatedProduct.getOtherProduct(product1);
        assertEquals(product2, otherProduct, "getOtherProduct should return product2 when product1 is passed");
    }

    @Test
    void testGetOtherProductWithProduct2() {
        Product otherProduct = relatedProduct.getOtherProduct(product2);
        assertEquals(product1, otherProduct, "getOtherProduct should return product1 when product2 is passed");
    }

    @Test
    void testGetOtherProductWithInvalidProduct() {
        Product invalidProduct = new Product("InvalidProduct", 30.0f, ProductTemperature.FROZEN, "path/to/image3.jpg");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            relatedProduct.getOtherProduct(invalidProduct);
        });
        assertEquals("Product p is not contained in this class", exception.getMessage(), "Exception message should indicate the product is not contained in the class");
    }

    @Test
    void testContainsWithProduct1() {
        assertTrue(relatedProduct.contains(product1), "contains should return true for product1");
    }

    @Test
    void testContainsWithProduct2() {
        assertTrue(relatedProduct.contains(product2), "contains should return true for product2");
    }

    @Test
    void testContainsWithDifferentProduct() {
        Product differentProduct = new Product("DifferentProduct", 30.0f, ProductTemperature.FROZEN, "path/to/image3.jpg");
        assertFalse(relatedProduct.contains(differentProduct), "contains should return false for a product not in the related products");
    }

    @Test
    void testConstructorThrowsExceptionWhenProduct1IsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RelatedProduct(null, product2, 0.2f);
        });
        assertEquals("Neither product1 nor product2 are null", exception.getMessage(), "Constructor should throw an exception when product1 is null");
    }

    @Test
    void testConstructorThrowsExceptionWhenProductsAreTheSame() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RelatedProduct(product1, product1, 0.5f);
        });
        assertEquals("Product 1 and Product 2 are the same",
                exception.getMessage(),
                "Constructor should throw an exception when both products are the same");
    }

    @Test
    void testConstructorThrowsExceptionWhenValueOutOfBounds() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RelatedProduct(product1, product2, -0.1f);
        });
        assertEquals("Value must be a float between 0 and 1.0, both included",
                exception.getMessage(),
                "Constructor should throw an exception when value is less than 0.0");

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new RelatedProduct(product1, product2, 1.1f);
        });
        assertEquals("Value must be a float between 0 and 1.0, both included",
                exception.getMessage(),
                "Constructor should throw an exception when value is greater than 1.0");
    }

    @Test
    void testConstructorThrowsExceptionWhenProduct2IsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RelatedProduct(product1, null, 5.0f);
        });
        assertEquals("Neither product1 nor product2 are null", exception.getMessage(), "Constructor should throw an exception when product2 is null");
    }

    @Test
    void testSetValue() {
        relatedProduct.setValue(0.1f);
        assertEquals(0.1f, relatedProduct.getValue(), "The value should be updated to 0.1");

        assertThrows(IllegalArgumentException.class,
                () -> relatedProduct.setValue(-10.0f),
                "Setting value to -10.0 should throw an IllegalArgumentException since it is less than 0.0");

        assertThrows(IllegalArgumentException.class,
                () -> relatedProduct.setValue(10.0f),
                "Setting value to 10.0 should throw an IllegalArgumentException since it is greater than 1.0");
    }
}
