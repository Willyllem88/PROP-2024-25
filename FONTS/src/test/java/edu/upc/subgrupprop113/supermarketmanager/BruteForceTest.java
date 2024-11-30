package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import edu.upc.subgrupprop113.supermarketmanager.services.BruteForce;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

public class BruteForceTest {
    private BruteForce bruteForce;
    private List<ShelvingUnit> shelvingUnits;
    private List<Product> products;
    private Product productA, productC, productD, productE;

    @Before
    public void setUp() {
        bruteForce = new BruteForce();

        // Setup Shelving Units
        shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(1, 3, ProductTemperature.REFRIGERATED));
        shelvingUnits.add(new ShelvingUnit(2, 3, ProductTemperature.FROZEN));
        shelvingUnits.add(new ShelvingUnit(3, 3, ProductTemperature.AMBIENT));

        // Setup Products
        products = new ArrayList<>();

        productA = new Product("Product A", 10.0f, ProductTemperature.REFRIGERATED, "path/to/imageA.png");
        Product productB = new Product("Product B", 15.0f, ProductTemperature.AMBIENT, "path/to/imageB.png");
        productC = new Product("Product C", 7.5f, ProductTemperature.FROZEN, "path/to/imageC.png");
        productD = new Product("Product D", 8.0f, ProductTemperature.AMBIENT, "path/to/imageD.png");
        productE = new Product("Product E", 12.0f, ProductTemperature.FROZEN, "path/to/imageE.png");

        this.products.add(productA);
        this.products.add(productB);
        this.products.add(productC);
        this.products.add(productD);

        new RelatedProduct(productA, productB, 0.1f);
        new RelatedProduct(productA, productC, 0.15f);
        new RelatedProduct(productA, productD, 0.2f);
        new RelatedProduct(productB, productC, 0.35f);
        new RelatedProduct(productB, productD, 0.25f);
        new RelatedProduct(productC, productD, 0.3f);
        new RelatedProduct(productA, productE, 0.3f);
        new RelatedProduct(productB, productE, 0.1f);
        new RelatedProduct(productC, productE, 0.3f);
        new RelatedProduct(productD, productE, 0.5f);
    }

    @Test
    public void testExpectedDistribution() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, this.products);
        double totalSimilarity = calculateTotalSimilarity(result);

        assertEquals("Total similarity should be ", 0.75, totalSimilarity, 0.0);
    }

    @Test
    public void testAllProductsInCompatibleShelves() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);

        // Check if all products are placed in compatible shelves
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    assertEquals("Product should be placed in a shelf with compatible temperature", shelf.getTemperature(), product.getTemperature());
                }
            }
        }
    }

    @Test
    public void testDuplicateProductsPlacement() {
        // Adding duplicate products to the list
        products.add(productC);
        products.add(productA);
        products.add(productA);

        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);
        double totalSimilarity = calculateTotalSimilarity(result);

        assertEquals("Total similarity should be ", 2.2, totalSimilarity, 0.1);
    }

    @Test
    public void testMoreProductsThanShelves() {
        // Adding more products to create a scenario where there are more products than shelves
        products.add(productC);
        products.add(productC);
        products.add(productA);
        products.add(productA);
        products.add(productE);
        products.add(productD);

        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);
        double totalSimilarity = calculateTotalSimilarity(result);

        assertEquals("Total similarity should be ", 3.9, totalSimilarity, 0.1);
    }
}
