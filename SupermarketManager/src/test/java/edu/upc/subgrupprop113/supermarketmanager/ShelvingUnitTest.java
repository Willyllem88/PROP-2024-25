package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShelvingUnitTest {
    private ShelvingUnit shelvingUnit;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Creation of the ShelvingUnit with three 3 levels and FRIDGE temperature
        shelvingUnit = new ShelvingUnit(1, 3, ProductTemperature.REFRIGERATED);

        // Creation of product examples
        product1 = new Product("Milk", 1.5f, ProductTemperature.REFRIGERATED, "/images/milk.jpg");
        product2 = new Product("Ice Cream", 3.0f, ProductTemperature.REFRIGERATED, "/images/icecream.jpg");
    }

    @Test
    void testGetUid() {
        assertEquals(1, shelvingUnit.getUid(), "Shelving unit UID should be 1");
    }

    @Test
    void testInitialShelvingUnitIsEmpty() {
        for (int i = 0; i < shelvingUnit.getHeight(); i++) {
            assertNull(shelvingUnit.getProduct(i), "Product at height " + i + " should be null initially");
        }
    }

    @Test
    void testAddProduct() {
        shelvingUnit.addProduct(product1, 0);
        assertEquals(product1, shelvingUnit.getProduct(0), "Product at height 0 should be Milk");
    }

    @Test
    void testAddProductAtInvalidHeight() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            shelvingUnit.addProduct(product1, -1);
        });
        assertEquals("Invalid height: -1", exception.getMessage(), "Exception message should match for invalid height");

        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            shelvingUnit.addProduct(product1, 5);
        });
        assertEquals("Invalid height: 5", exception.getMessage(), "Exception message should match for height out of range");
    }

    @Test
    void testAddNullProduct() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            shelvingUnit.addProduct(null, 0);
        });
        assertEquals("Product cannot be null.", exception.getMessage(), "Exception message should match for null product");
    }

    @Test
    void testRemoveProduct() {
        shelvingUnit.addProduct(product1, 0);
        shelvingUnit.removeProduct(0);
        assertNull(shelvingUnit.getProduct(0), "Product at height 0 should be null after removal");
    }

    @Test
    void testRemoveProductAtInvalidHeight() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            shelvingUnit.removeProduct(-1);
        });
        assertEquals("Invalid height: -1", exception.getMessage(), "Exception message should match for invalid height removal");

        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            shelvingUnit.removeProduct(5);
        });
        assertEquals("Invalid height: 5", exception.getMessage(), "Exception message should match for height out of range removal");
    }

    @Test
    void testEmptyShelvingUnit() {
        shelvingUnit.addProduct(product1, 0);
        shelvingUnit.addProduct(product2, 1);
        shelvingUnit.emptyShelvingUnit();
        for (int i = 0; i < shelvingUnit.getHeight(); i++) {
            assertNull(shelvingUnit.getProduct(i), "All products should be null after emptying the shelving unit");
        }
    }

    @Test
    void testSetTemperature() {
        shelvingUnit.setTemperature(ProductTemperature.REFRIGERATED);
        assertEquals(ProductTemperature.REFRIGERATED, shelvingUnit.getTemperature(), "Temperature should be set to FREEZER");
    }
}
