package edu.upc.subgrupprop113.supermarketmanager;

import static org.junit.Assert.*;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RelatedProductTest {
    private Product product1;
    private Product product2;
    private RelatedProduct relatedProduct;

    @Before
    public void setUp() {
        // Initialize products with dummy data
        product1 = new Product("Product1", 10.0f, ProductTemperature.FROZEN, "path/to/image1.jpg");
        product2 = new Product("Product2", 20.0f, ProductTemperature.FROZEN, "path/to/image2.jpg");
        relatedProduct = new RelatedProduct(product1, product2, 0.5f);
    }

    @Test
    public void testGetValue() {
        assertEquals("The value should be 0.5", 0.5f, relatedProduct.getValue(), 0.01f);
    }

    @Test
    public void testGetProducts() {
        List<Product> products = relatedProduct.getProducts();
        assertEquals("The list should contain exactly two products", 2, products.size());
        assertTrue("The list should contain product1", products.contains(product1));
        assertTrue("The list should contain product2", products.contains(product2));
    }

    @Test
    public void testGetOtherProductWithProduct1() {
        Product otherProduct = relatedProduct.getOtherProduct(product1);
        assertEquals("getOtherProduct should return product2 when product1 is passed", product2, otherProduct);
    }

    @Test
    public void testGetOtherProductWithProduct2() {
        Product otherProduct = relatedProduct.getOtherProduct(product2);
        assertEquals("getOtherProduct should return product1 when product2 is passed", product1, otherProduct);
    }

    @Test
    public void testGetOtherProductWithInvalidProduct() {
        Product invalidProduct = new Product("InvalidProduct", 30.0f, ProductTemperature.FROZEN, "path/to/image3.jpg");
        try {
            relatedProduct.getOtherProduct(invalidProduct);
            fail("Exception should be thrown for invalid product");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testContainsWithProduct1() {
        assertTrue("contains should return true for product1", relatedProduct.contains(product1));
    }

    @Test
    public void testContainsWithProduct2() {
        assertTrue("contains should return true for product2", relatedProduct.contains(product2));
    }

    @Test
    public void testContainsWithDifferentProduct() {
        Product differentProduct = new Product("DifferentProduct", 30.0f, ProductTemperature.FROZEN, "path/to/image3.jpg");
        assertFalse("contains should return false for a product not in the related products", relatedProduct.contains(differentProduct));
    }

    @Test
    public void testConstructorThrowsExceptionWhenProduct1IsNull() {
        try {
            new RelatedProduct(null, product2, 0.2f);
            fail("Constructor should throw an exception when product1 is null");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testConstructorThrowsExceptionWhenProduct2IsNull() {
        try {
            new RelatedProduct(product1, null, 5.0f);
            fail("Constructor should throw an exception when product2 is null");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testConstructorThrowsExceptionWhenProductsAreTheSame() {
        try {
            new RelatedProduct(product1, product1, 0.5f);
            fail("Constructor should throw an exception when both products are the same");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testConstructorThrowsExceptionWhenValueOutOfBounds() {
        try {
            new RelatedProduct(product1, product2, -0.1f);
            fail("Constructor should throw an exception when value is less than 0.0");
        } catch (IllegalArgumentException e) { }

        try {
            new RelatedProduct(product1, product2, 1.1f);
            fail("Constructor should throw an exception when value is greater than 1.0");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testSetValue() {
        relatedProduct.setValue(0.1f);
        assertEquals("The value should be updated to 0.1", 0.1f, relatedProduct.getValue(), 0.01f);

        try {
            relatedProduct.setValue(-10.0f);
            fail("Setting value to -10.0 should throw an IllegalArgumentException since it is less than 0.0");
        } catch (IllegalArgumentException e) {
            // Excepción esperada
        }

        try {
            relatedProduct.setValue(10.0f);
            fail("Setting value to 10.0 should throw an IllegalArgumentException since it is greater than 1.0");
        } catch (IllegalArgumentException e) {
            // Excepción esperada
        }
    }
}
