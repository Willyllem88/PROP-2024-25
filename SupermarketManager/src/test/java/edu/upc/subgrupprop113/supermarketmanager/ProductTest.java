package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product productA, productB, productC;
    private RelatedProduct relatedProductAB, relatedProductAC, relatedProductBC;


    @BeforeEach
    void setUp() {
        productA = new Product("Product A", 10.0f, ProductTemperature.FROZEN, "path/to/imageA.png");
        productB = new Product("Product B", 15.0f, ProductTemperature.AMBIENT, "path/to/imageB.png");
        productC = new Product("Product C", 7.5f, ProductTemperature.REFRIGERATED, "path/to/imageC.png");
        relatedProductAB = new RelatedProduct(productA, productB, 0.4f); // Assume RelatedProduct constructor exists
        relatedProductAC = new RelatedProduct(productA, productC, 0.8f); // Assume RelatedProduct constructor exists
        relatedProductBC = new RelatedProduct(productB, productC, 0.2f); // Assume RelatedProduct constructor exists
    }

    @Test
    void testAddKeyWord() {
        productA.addKeyWord("Fresh");
        productA.addKeyWord("Organic");

        assertEquals(2, productA.getKeyWords().size(), "Expected 2 keywords to be added to Product A.");
        assertTrue(productA.getKeyWords().contains("Fresh"), "Product A should contain the keyword 'Fresh'.");
        assertTrue(productA.getKeyWords().contains("Organic"), "Product A should contain the keyword 'Organic'.");
    }


    // addRelatedProduct(...) cannot be handled directly by the programmer, so it won't be tested. It is
    // used in new RelatedProduct(productA, productB, 0.4f), that function calls it twice, for each product;

    @Test
    void testGetRelatedProduct() {
        assertEquals(0.4f, productA.getRelatedValue(productB), 0.01f, "The related value between Product A and Product B should be 0.4.");
        assertThrows(IllegalArgumentException.class, () -> productA.addRelatedProduct(relatedProductAB), "Adding a related product should throw an IllegalArgumentException.");
    }

    @Test
    void testSetRelatedValue() {
        productA.setRelatedValue(productB, 0.9f);

        assertEquals(0.9f, productA.getRelatedValue(productB), 0.01, "The related value between Product A and Product B should be updated to 0.9.");
    }

    @Test
    void testEliminateAllRelations() {
        productA.eliminateAllRelations();

        assertThrows(IllegalArgumentException.class, () -> productA.getRelatedValue(productB), "Getting the related value for Product A and Product B should throw an IllegalArgumentException after relations are eliminated.");
        assertThrows(IllegalArgumentException.class, () -> productB.getRelatedValue(productA), "Getting the related value for Product B and Product A should throw an IllegalArgumentException after relations are eliminated.");

        assertEquals(0.2f, productB.getRelatedValue(productC), 0.01f, "The related value between Product B and Product C should remain 0.2 after eliminating relations with Product A.");
    }
}
