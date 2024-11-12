package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class GreedyBacktrackingTest {
    private GreedyBacktracking greedyBacktracking;
    private List<ShelvingUnit> shelvingUnits;
    private List<Product> products;
    private Product productA, productB, productC, productD, productE;

    @Before
    public void setUp() {
        greedyBacktracking = new GreedyBacktracking();

        // Setup Shelving Units
        shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(1, 3, ProductTemperature.REFRIGERATED));
        shelvingUnits.add(new ShelvingUnit(2, 3, ProductTemperature.FROZEN));
        shelvingUnits.add(new ShelvingUnit(3, 3, ProductTemperature.AMBIENT));

        // Setup Products
        products = new ArrayList<>();

        productA = new Product("Product A", 10.0f, ProductTemperature.REFRIGERATED, "path/to/imageA.png");
        productB = new Product("Product B", 15.0f, ProductTemperature.AMBIENT, "path/to/imageB.png");
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
        ArrayList<ShelvingUnit> result = greedyBacktracking.orderSupermarket(shelvingUnits, this.products);

        // Check the expected distribution
        assertEquals("Product A should be placed at Shelving Unit 1, height 2", productA, result.getFirst().getProduct(2));
        assertNull("Shelving Unit 1, height 1 should be empty", result.getFirst().getProduct(1));
        assertNull("Shelving Unit 1, height 0 should be empty", result.getFirst().getProduct(0));

        assertEquals("Product C should be placed at Shelving Unit 2, height 2", productC, result.get(1).getProduct(2));
        assertNull("Shelving Unit 2, height 1 should be empty", result.get(1).getProduct(1));
        assertNull("Shelving Unit 2, height 0 should be empty", result.get(1).getProduct(0));

        assertEquals("Product B should be placed at Shelving Unit 3, height 2", productB, result.get(2).getProduct(2));
        assertEquals("Product D should be placed at Shelving Unit 3, height 1", productD, result.get(2).getProduct(1));
        assertNull("Shelving Unit 3, height 0 should be empty", result.get(2).getProduct(0));
    }

    @Test
    public void testAllProductsInCompatibleShelves() {
        ArrayList<ShelvingUnit> result = greedyBacktracking.orderSupermarket(shelvingUnits, products);

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

        ArrayList<ShelvingUnit> result = greedyBacktracking.orderSupermarket(shelvingUnits, products);

        // Check the expected distribution with duplicates
        assertEquals("Product A should be placed at Shelving Unit 1, height 2", productA, result.getFirst().getProduct(2));
        assertEquals("Product A should be placed at Shelving Unit 1, height 1", productA, result.getFirst().getProduct(1));
        assertEquals("Product A should be placed at Shelving Unit 1, height 0", productA, result.getFirst().getProduct(0));

        assertEquals("Product C should be placed at Shelving Unit 2, height 2", productC, result.get(1).getProduct(2));
        assertEquals("Product C should be placed at Shelving Unit 2, height 1", productC, result.get(1).getProduct(1));
        assertNull("Shelving Unit 2, height 0 should be empty", result.get(1).getProduct(0));

        assertEquals("Product B should be placed at Shelving Unit 3, height 2", productB, result.get(2).getProduct(2));
        assertEquals("Product D should be placed at Shelving Unit 3, height 1", productD, result.get(2).getProduct(1));
        assertNull("Shelving Unit 3, height 0 should be empty", result.get(2).getProduct(0));
    }

    @Test
    public void testMoreProductsThanShelves() {
        // Adding more products to create a scenario where there are more products than shelves
        products.add(productC); // Create a new instance of productC
        products.add(productC); // Another new instance
        products.add(productA); // New instance of productA
        products.add(productA); // Another new instance
        products.add(productE); // Adding new productE

        ArrayList<ShelvingUnit> result = greedyBacktracking.orderSupermarket(shelvingUnits, products);
        printDistribution(result);

        // Check the expected distribution with extra products
        assertEquals("Product A should be placed at Shelving Unit 1, height 2", productA, result.getFirst().getProduct(2));
        assertEquals("Product A should be placed at Shelving Unit 1, height 1", productA, result.getFirst().getProduct(1));
        assertEquals("Product A should be placed at Shelving Unit 1, height 0", productA, result.getFirst().getProduct(0));

        assertEquals("Product E should be placed at Shelving Unit 2, height 2", productE, result.get(1).getProduct(2));
        assertEquals("Product C should be placed at Shelving Unit 2, height 1", productC, result.get(1).getProduct(1));
        assertEquals("Product C should be placed at Shelving Unit 2, height 0", productC, result.get(1).getProduct(0));

        assertEquals("Product D should be placed at Shelving Unit 3, height 2", productD, result.get(2).getProduct(2));
        assertEquals("Product B should be placed at Shelving Unit 3, height 1", productB, result.get(2).getProduct(1));
        assertNull("Shelving Unit 3, height 0 should be empty", result.get(2).getProduct(0));
    }

    private void printDistribution(ArrayList<ShelvingUnit> result) {
        for (ShelvingUnit shelf : result) {
            for (int i = shelf.getHeight() - 1; i >= 0; i--) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    System.out.println("Shelf " + shelf.getUid() + ", Height " + i + ": " + product.getName());
                }
                else {
                    System.out.println("Shelf " + shelf.getUid() + ", Height " + i + ": Empty");
                }
            }
        }
    }
}
