package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import edu.upc.subgrupprop113.supermarketmanager.services.Approximation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

public class ApproximationTest {
    private Approximation approximation;
    private List<ShelvingUnit> shelvingUnits;
    private List<ShelvingUnit> shelvingUnitsTemperatures;
    private List<Product> products;
    private Product productA, productB, productC, productD, productE, productF, productG, productH, productI, productJ, productK;

    @Before
    public void setUp() {
        approximation = new Approximation();

        int shelfHeight = 3;

        // Set up Shelving Units
        shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(1, shelfHeight, ProductTemperature.AMBIENT));
        shelvingUnits.add(new ShelvingUnit(2, shelfHeight, ProductTemperature.AMBIENT));
        shelvingUnits.add(new ShelvingUnit(3, shelfHeight, ProductTemperature.AMBIENT));

        shelvingUnitsTemperatures = new ArrayList<>();
        shelvingUnitsTemperatures.add(new ShelvingUnit(1, shelfHeight, ProductTemperature.AMBIENT));
        shelvingUnitsTemperatures.add(new ShelvingUnit(2, shelfHeight, ProductTemperature.REFRIGERATED));
        shelvingUnitsTemperatures.add(new ShelvingUnit(3, shelfHeight, ProductTemperature.FROZEN));


        // Set up Products
        products = new ArrayList<>();
        productA = new Product("Product A", 10.0f, ProductTemperature.AMBIENT, "path/to/imageA.png");
        productB = new Product("Product B", 15.0f, ProductTemperature.AMBIENT, "path/to/imageB.png");
        productC = new Product("Product C", 7.5f, ProductTemperature.AMBIENT, "path/to/imageC.png");
        productD = new Product("Product D", 8.0f, ProductTemperature.AMBIENT, "path/to/imageD.png");
        productE = new Product("Product E", 12.0f, ProductTemperature.AMBIENT, "path/to/imageE.png");
        productF = new Product("Product F", 11.0f, ProductTemperature.AMBIENT, "path/to/imageF.png");
        productG = new Product("Product G", 9.0f, ProductTemperature.AMBIENT, "path/to/imageG.png");
        productH = new Product("Product H", 9.0f, ProductTemperature.AMBIENT, "path/to/imageH.png");
        productI = new Product("Product I", 9.0f, ProductTemperature.AMBIENT, "path/to/imageI.png");
        productJ = new Product("Product J", 9.0f, ProductTemperature.AMBIENT, "path/to/imageJ.png");
        productK = new Product("Product K", 9.0f, ProductTemperature.AMBIENT, "path/to/imageK.png");
//        productH = new Product("Product H", 9.0f, ProductTemperature.REFRIGERATED, "path/to/imageH.png");
//        productI = new Product("Product I", 9.0f, ProductTemperature.FROZEN, "path/to/imageI.png");
//        productJ = new Product("Product J", 9.0f, ProductTemperature.FROZEN, "path/to/imageJ.png");
//        productK = new Product("Product K", 9.0f, ProductTemperature.REFRIGERATED, "path/to/imageK.png");


        products.add(productA);
        products.add(productB);
        products.add(productC);
        products.add(productD);

//        products.add(productE);
//        products.add(productF);
//        products.add(productG);
//        products.add(productH);
//        products.add(productI);
//        products.add(productJ);
//        products.add(productK);

        new RelatedProduct(productA, productB, 0.1f);
        new RelatedProduct(productA, productC, 0.15f);
        new RelatedProduct(productA, productD, 0.2f);
        new RelatedProduct(productA, productE, 0.3f);
        new RelatedProduct(productA, productF, 0.2f);
        new RelatedProduct(productA, productG, 0.09f);
        new RelatedProduct(productA, productH, 0.5f);
        new RelatedProduct(productA, productI, 0.14f);
        new RelatedProduct(productA, productJ, 0.1f);
        new RelatedProduct(productA, productK, 0.1f);
        new RelatedProduct(productB, productC, 0.35f);
        new RelatedProduct(productB, productD, 0.25f);
        new RelatedProduct(productB, productE, 0.1f);
        new RelatedProduct(productB, productF, 0.1f);
        new RelatedProduct(productB, productG, 0.2f);
        new RelatedProduct(productB, productH, 0.05f);
        new RelatedProduct(productB, productI, 0.1f);
        new RelatedProduct(productB, productJ, 0.12f);
        new RelatedProduct(productB, productK, 0.23f);
        new RelatedProduct(productC, productD, 0.3f);
        new RelatedProduct(productC, productE, 0.3f);
        new RelatedProduct(productC, productF, 0.3f);
        new RelatedProduct(productC, productG, 0.3f);
        new RelatedProduct(productC, productH, 0.15f);
        new RelatedProduct(productC, productI, 0.8f);
        new RelatedProduct(productC, productJ, 0.3f);
        new RelatedProduct(productC, productK, 0.4f);
        new RelatedProduct(productD, productE, 0.5f);
        new RelatedProduct(productD, productF, 0.4f);
        new RelatedProduct(productD, productG, 0.4f);
        new RelatedProduct(productD, productH, 0.2f);
        new RelatedProduct(productD, productI, 0.3f);
        new RelatedProduct(productD, productJ, 0.3f);
        new RelatedProduct(productD, productK, 0.3f);
        new RelatedProduct(productE, productF, 0.5f);
        new RelatedProduct(productE, productG, 0.6f);
        new RelatedProduct(productE, productH, 0.3f);
        new RelatedProduct(productE, productI, 0.2f);
        new RelatedProduct(productE, productJ, 0.12f);
        new RelatedProduct(productE, productK, 0.22f);
        new RelatedProduct(productF, productG, 0.7f);
        new RelatedProduct(productF, productH, 0.3f);
        new RelatedProduct(productF, productI, 0.2f);
        new RelatedProduct(productF, productJ, 0.1f);
        new RelatedProduct(productF, productK, 0.1f);
        new RelatedProduct(productG, productH, 0.3f);
        new RelatedProduct(productG, productI, 0.2f);
        new RelatedProduct(productG, productJ, 0.1f);
        new RelatedProduct(productG, productK, 0.1f);
        new RelatedProduct(productH, productI, 0.3f);
        new RelatedProduct(productH, productJ, 0.2f);
        new RelatedProduct(productH, productK, 0.1f);
        new RelatedProduct(productI, productJ, 0.3f);
        new RelatedProduct(productI, productK, 0.2f);
        new RelatedProduct(productJ, productK, 0.1f);
    }

    @Test
    public void testIncreasedSimilarityScore() {
        shelvingUnits.get(0).addProduct(productA, 2);
        shelvingUnits.get(1).addProduct(productB, 2);
        shelvingUnits.get(2).addProduct(productC, 2);
        shelvingUnits.get(2).addProduct(productD, 1);
        double initialScore = calculateTotalSimilarity((ArrayList<ShelvingUnit>) shelvingUnits);

        // Run the simulated annealing algorithm
        ArrayList<ShelvingUnit> result = approximation.orderSupermarket(shelvingUnits, products);
        double resultScore = calculateTotalSimilarity(result);

        assertTrue("Optimized similarity score should be higher than the initial score", resultScore >= initialScore);
    }

    @Test
    public void testPerformanceWithExtraProducts() {
        // Adding additional products
        products.add(productE);
        products.add(productF);
        products.add(productG);

        shelvingUnits.get(0).addProduct(productA, 2);
        shelvingUnits.get(1).addProduct(productB, 2);
        shelvingUnits.get(2).addProduct(productC, 2);
        shelvingUnits.get(2).addProduct(productD, 1);
        shelvingUnits.get(1).addProduct(productE, 2);
        shelvingUnits.get(0).addProduct(productF, 2);
        shelvingUnits.get(0).addProduct(productG, 1);

        double initialScore = calculateTotalSimilarity((ArrayList<ShelvingUnit>) shelvingUnits);

        // Run the simulated annealing algorithm
        ArrayList<ShelvingUnit> result = approximation.orderSupermarket(shelvingUnits, products);
        double resultScore = calculateTotalSimilarity(result);

        assertTrue("Optimized similarity score should be higher than the initial score", resultScore >= initialScore);
    }

    @Test
    public void testWithDuplicatedProducts()
    {
        products.add(productA);
        products.add(productB);

        shelvingUnits.get(0).addProduct(productA, 2);
        shelvingUnits.get(1).addProduct(productB, 2);
        shelvingUnits.get(2).addProduct(productC, 2);
        shelvingUnits.get(2).addProduct(productD, 1);
        shelvingUnits.get(1).addProduct(productA, 1);
        shelvingUnits.get(0).addProduct(productB, 1);

        double initialScore = calculateTotalSimilarity((ArrayList<ShelvingUnit>) shelvingUnits);

        // Run the simulated annealing algorithm
        ArrayList<ShelvingUnit> result = approximation.orderSupermarket(shelvingUnits, products);
        double resultScore = calculateTotalSimilarity(result);

        // Check if the similarity score increased
        assertTrue("Optimized similarity score should be higher than the initial score", resultScore >= initialScore);
    }

    @Test
    public void testProductCompatibilityWithShelfTemperature() {
        // Run the simulated annealing algorithm

        products.add(productH);
        products.add(productI);
        products.add(productJ);
        products.add(productK);

        ArrayList<ShelvingUnit> result = approximation.orderSupermarket(shelvingUnitsTemperatures, products);

        // Check if all products are placed in compatible shelves
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    assertEquals("Product should be placed in a shelf with compatible temperature",
                            shelf.getTemperature(), product.getTemperature());
                }
            }
        }
    }

    @Test
    public void testMoreProductsThanShelves()
    {
        products.add(productE);
        products.add(productF);
        products.add(productG);
        products.add(productH);
        products.add(productI);
        products.add(productJ);
        products.add(productK);

        ArrayList<ShelvingUnit> result = approximation.orderSupermarket(shelvingUnits, products);

        int[] quantities = new int[3];
        for (Product p : products) {
            if (p.getTemperature() == ProductTemperature.AMBIENT) {
                quantities[0]++;
            } else if (p.getTemperature() == ProductTemperature.REFRIGERATED) {
                quantities[1]++;
            } else {
                quantities[2]++;
            }
        }

        int[] shelvesSpace = new int[3];
        for (ShelvingUnit s : result) {
            if (s.getTemperature() == ProductTemperature.AMBIENT) {
                shelvesSpace[0]++;
            } else if (s.getTemperature() == ProductTemperature.REFRIGERATED) {
                shelvesSpace[1]++;
            } else {
                shelvesSpace[2]++;
            }
        }

        int shelfHeight = result.getFirst().getHeight();
        shelvesSpace[0] *= shelfHeight;
        shelvesSpace[1] *= shelfHeight;
        shelvesSpace[2] *= shelfHeight;


        // Check if all products are placed in compatible shelves
        int totalPlacedProducts = 0;
        for (ShelvingUnit shelf : result) {
            for (int i = 0; i < shelf.getHeight(); i++) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    ++totalPlacedProducts;
                }
            }
        }

        int totalPlaceableProducts = 0;
        for (int i = 0; i < 3; i++) {
            totalPlaceableProducts += Math.min(quantities[i], shelvesSpace[i]);
        }

        assertEquals("All products should be placed", totalPlacedProducts, totalPlaceableProducts);
    }
}
