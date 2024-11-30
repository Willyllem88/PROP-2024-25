package edu.upc.subgrupprop113.supermarketmanager;

import static org.junit.Assert.*;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import org.junit.Before;
import org.junit.Test;

public class ShelvingUnitTest {
    private ShelvingUnit shelvingUnit;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() {
        // Creation of the ShelvingUnit with three 3 levels and FRIDGE temperature
        shelvingUnit = new ShelvingUnit(1, 3, ProductTemperature.REFRIGERATED);

        // Creation of product examples
        product1 = new Product("Milk", 1.5f, ProductTemperature.REFRIGERATED, "/images/milk.jpg");
        product2 = new Product("Ice Cream", 3.0f, ProductTemperature.REFRIGERATED, "/images/icecream.jpg");
    }

    @Test
    public void testGetUid() {
        assertEquals("Shelving unit UID should be 1", 1, shelvingUnit.getUid());
    }

    @Test
    public void testInitialShelvingUnitIsEmpty() {
        for (int i = 0; i < shelvingUnit.getHeight(); i++) {
            assertNull("Product at height " + i + " should be null initially", shelvingUnit.getProduct(i));
        }
    }

    @Test
    public void testAddProduct() {
        shelvingUnit.addProduct(product1, 0);
        assertEquals("Product at height 0 should be Milk", product1, shelvingUnit.getProduct(0));
    }

    @Test
    public void testAddProductAtInvalidHeight() {
        try {
            shelvingUnit.addProduct(product1, -1);
            fail("Exception should be thrown for invalid height");
        } catch (IndexOutOfBoundsException e) { }

        try {
            shelvingUnit.addProduct(product1, 5);
            fail("Exception should be thrown for height out of range");
        } catch (IndexOutOfBoundsException e) { }
    }

    @Test
    public void testAddNullProduct() {
        try {
            shelvingUnit.addProduct(null, 0);
            fail("Exception should be thrown for null product");
        } catch (NullPointerException e) { }
    }

    @Test
    public void testRemoveProduct() {
        shelvingUnit.addProduct(product1, 0);
        shelvingUnit.removeProduct(0);
        assertNull("Product at height 0 should be null after removal", shelvingUnit.getProduct(0));
    }

    @Test
    public void testRemoveProductAtInvalidHeight() {
        try {
            shelvingUnit.removeProduct(-1);
            fail("Exception should be thrown for invalid height removal");
        } catch (IndexOutOfBoundsException e) { }

        try {
            shelvingUnit.removeProduct(5);
            fail("Exception should be thrown for height out of range removal");
        } catch (IndexOutOfBoundsException e) { }
    }

    @Test
    public void testEmptyShelvingUnit() {
        shelvingUnit.addProduct(product1, 0);
        shelvingUnit.addProduct(product2, 1);
        shelvingUnit.emptyShelvingUnit();
        for (int i = 0; i < shelvingUnit.getHeight(); i++) {
            assertNull("All products should be null after emptying the shelving unit", shelvingUnit.getProduct(i));
        }
    }

    @Test
    public void testSetTemperature() {
        shelvingUnit.setTemperature(ProductTemperature.REFRIGERATED);
        assertEquals("Temperature should be set to REFRIGERATED", ProductTemperature.REFRIGERATED, shelvingUnit.getTemperature());
    }
}
