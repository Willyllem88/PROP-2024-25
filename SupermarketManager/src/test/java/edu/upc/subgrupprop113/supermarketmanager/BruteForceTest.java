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
    private RelatedProduct relatedProductAB, relatedProductAC, relatedProductBC, relatedProductAD, relatedProductAE, relatedProductAF, relatedProductBD, relatedProductBE, relatedProductBF, relatedProductCD, relatedProductCE, relatedProductCF, relatedProductDE, relatedProductDF, relatedProductEF;
    private Product productA, productB, productC, productD, productE, productF;

    @Before
    public void setUp() {
        bruteForce = new BruteForce();

        // Setup Shelving Units
        shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(1, 3, ProductTemperature.AMBIENT));
        shelvingUnits.add(new ShelvingUnit(2, 3, ProductTemperature.FROZEN));
        shelvingUnits.add(new ShelvingUnit(3, 3, ProductTemperature.REFRIGERATED));

        // Setup Products
        products = new HashSet<>();
        productA = new Product("Product A", 10.0f, ProductTemperature.AMBIENT, "path/to/imageA.png");
        productB = new Product("Product B", 15.0f, ProductTemperature.FROZEN, "path/to/imageB.png");
        productC = new Product("Product C", 7.5f, ProductTemperature.REFRIGERATED, "path/to/imageC.png");
        productD = new Product("Product D", 8.0f, ProductTemperature.AMBIENT, "path/to/imageD.png");
        productE = new Product("Product E", 12.0f, ProductTemperature.FROZEN, "path/to/imageE.png");
        productF = new Product("Product F", 9.0f, ProductTemperature.REFRIGERATED, "path/to/imageF.png");
        products.add(productA);
        products.add(productB);
        products.add(productC);
        products.add(productD);
        products.add(productE);
        products.add(productF);

        // Setup Related Products
        relatedProductAB = new RelatedProduct(productA, productB, 0.4f);
        relatedProductAC = new RelatedProduct(productA, productC, 0.8f);
        relatedProductAD = new RelatedProduct(productA, productD, 0.5f);
        relatedProductAE = new RelatedProduct(productA, productE, 0.3f);
        relatedProductAF = new RelatedProduct(productA, productF, 0.7f);
        relatedProductBC = new RelatedProduct(productB, productC, 0.2f);
        relatedProductBD = new RelatedProduct(productB, productD, 0.6f);
        relatedProductBE = new RelatedProduct(productB, productE, 0.9f);
        relatedProductBF = new RelatedProduct(productB, productF, 0.1f);
        relatedProductCD = new RelatedProduct(productC, productD, 0.4f);
        relatedProductCE = new RelatedProduct(productC, productE, 0.5f);
        relatedProductCF = new RelatedProduct(productC, productF, 0.6f);
        relatedProductDE = new RelatedProduct(productD, productE, 0.2f);
        relatedProductDF = new RelatedProduct(productD, productF, 0.3f);
        relatedProductEF = new RelatedProduct(productE, productF, 0.8f);
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
        printFinalDistribution(result);
    }

    private void printFinalDistribution(ArrayList<ShelvingUnit> distribution) {
        for (int i = 0; i < distribution.size(); i++) {
            ShelvingUnit shelf = distribution.get(i);
            System.out.println("Shelving Unit " + shelf.getUid() + " (Temperature: " + shelf.getTemperature() + "):");
            for (int j = 0; j < shelf.getHeight(); j++) {
                Product product = shelf.getProduct(j);
                if (product != null) {
                    System.out.println("  Shelf position " + j + ": " + product.getName());
                } else {
                    System.out.println("  Shelf position " + j + ": Empty");
                }
            }
        }
    }


    @Test
    public void testProductsInCompatibleShelves() {
        ArrayList<ShelvingUnit> result = bruteForce.orderSupermarket(shelvingUnits, products);

        // Check if all products are placed in a compatible shelf
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    assertTrue("Product should be compatible with the shelf", shelf.getTemperature() == product.getTemperature());
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
