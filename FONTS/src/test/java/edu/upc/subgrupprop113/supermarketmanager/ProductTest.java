package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    private Product productA, productB, productC;
    private RelatedProduct relatedProductAB, relatedProductAC, relatedProductBC;


    @Before
    public void setUp() {
        productA = new Product("Product A", 10.0f, ProductTemperature.FROZEN, "path/to/imageA.png");
        productB = new Product("Product B", 15.0f, ProductTemperature.AMBIENT, "path/to/imageB.png");
        productC = new Product("Product C", 7.5f, ProductTemperature.REFRIGERATED, "path/to/imageC.png");
        relatedProductAB = new RelatedProduct(productA, productB, 0.4f); // Assume RelatedProduct constructor exists
        relatedProductAC = new RelatedProduct(productA, productC, 0.8f); // Assume RelatedProduct constructor exists
        relatedProductBC = new RelatedProduct(productB, productC, 0.2f); // Assume RelatedProduct constructor exists
    }

    @Test
    public void testAddKeyWord() {
        productA.addKeyWord("Fresh");
        productA.addKeyWord("Organic");

        assertEquals("Expected 2 keywords to be added to Product A.", 2, productA.getKeyWords().size());
        assertTrue("Product A should contain the keyword 'Fresh'.", productA.getKeyWords().contains("Fresh"));
        assertTrue("Product A should contain the keyword 'Organic'.", productA.getKeyWords().contains("Organic"));
    }

    // addRelatedProduct(...) cannot be handled directly by the programmer, so it won't be tested. It is
    // used in new RelatedProduct(productA, productB, 0.4f), that function calls it twice, for each product;

    @Test
    public void testGetRelatedProduct() {
        assertEquals("The related value between Product A and Product B should be 0.4.", 0.4f, productA.getRelatedValue(productB), 0.01f);

        try {
            productA.addRelatedProduct(relatedProductAB);
            fail("Adding a related product should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testSetRelatedValue() {
        productA.setRelatedValue(productB, 0.9f);

        assertEquals("The related value between Product A and Product B should be updated to 0.9.", 0.9f, productA.getRelatedValue(productB), 0.01f);
    }

    @Test
    public void testEliminateAllRelations() {
        productA.eliminateAllRelations();

        try {
            productA.getRelatedValue(productB);
            fail("Getting the related value for Product A and Product B should throw an IllegalArgumentException after relations are eliminated.");
        } catch (IllegalArgumentException e) { }

        try {
            productB.getRelatedValue(productA);
            fail("Getting the related value for Product B and Product A should throw an IllegalArgumentException after relations are eliminated.");
        } catch (IllegalArgumentException e) { }

        assertEquals("The related value between Product B and Product C should remain 0.2 after eliminating relations with Product A.", 0.2f, productB.getRelatedValue(productC), 0.01f);
    }
}
