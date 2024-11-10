package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BruteForceTest {
    private BruteForce bruteForce;
    private ArrayList<ShelvingUnit> shelvingUnits;
    private Set<Product> products;

    @Before
    public void setUp() {
        bruteForce = new BruteForce();

        // Setup Shelving Units
        shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(1, 3, ProductTemperature.AMBIENT));
        shelvingUnits.add(new ShelvingUnit(2, 3, ProductTemperature.FROZEN));
        shelvingUnits.add(new ShelvingUnit(3, 3, ProductTemperature.REFRIGERATED));
        shelvingUnits.add(new ShelvingUnit(4, 3, ProductTemperature.AMBIENT));

        // Setup Products
        products = new HashSet<>();
        products.add(new Product("Product A", 10.0f, ProductTemperature.AMBIENT, "path/to/imageA.png"));
        products.add(new Product("Product B", 15.0f, ProductTemperature.FROZEN, "path/to/imageB.png"));
        products.add(new Product("Product C", 7.5f, ProductTemperature.REFRIGERATED, "path/to/imageC.png"));
        products.add(new Product("Product D", 8.0f, ProductTemperature.AMBIENT, "path/to/imageD.png"));
        products.add(new Product("Product E", 12.0f, ProductTemperature.FROZEN, "path/to/imageE.png"));
        products.add(new Product("Product F", 9.0f, ProductTemperature.REFRIGERATED, "path/to/imageF.png"));
    }

    @Test
    public void testOrderSupermarket() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);

        // Check that all products are placed
        int productCount = 0;
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                if (shelf.getProduct(i) != null) {
                    productCount++;
                }
            }
        }
        assertEquals("All products should be placed", products.size(), productCount);
    }

    @Test
    public void testProductsInCompatibleShelves() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);

        // Check if all products are placed in a compatible shelf
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    assertSame("Product should be compatible with the shelf", shelf.getTemperature(), product.getTemperature());
                }
            }
        }
    }

    @Test
    public void testNoDuplicateProducts() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);
        Set<Product> placedProducts = new HashSet<>();

        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    assertFalse("Product should not be placed more than once", placedProducts.contains(product));
                    placedProducts.add(product);
                }
            }
        }
    }
}
