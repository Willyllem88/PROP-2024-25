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




































/*package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ProductTest {
    @Test
    public void testProductGettersAndSetters() {
        Product product = new Product("Product1", 10.5f, ProductTemperature.AMBIENT, "img/path");

        // Test name getter and setter
        product.setName("NewName");
        assertEquals("NewName", product.getName(), "Product name should be updated to 'NewName'");

        // Test price getter and setter
        product.setPrice(20.0f);
        assertEquals(20.0f, product.getPrice(), "Product price should be updated to 20.0f");

        // Test temperature getter and setter
        product.setTemperature(ProductTemperature.FROZEN);
        assertEquals(ProductTemperature.FROZEN, product.getTemperature(), "Product temperature should be updated to FROZEN");

        // Test image path getter and setter
        product.setImgPath("new/img/path");
        assertEquals("new/img/path", product.getImgPath(), "Product image path should be updated to 'new/img/path'");
    }

    @Test
    public void testKeyWords() {
        Product product = new Product("Product1", 10.5f, ProductTemperature.AMBIENT, "img/path");

        // Test adding a keyword and retrieving it
        product.addKeyWord("key1");
        List<String> keyWords = product.getKeyWords();
        assertEquals(1, keyWords.size(), "Product should have one key word");
        assertTrue(keyWords.contains("key1"), "Product should contain key word 'key1'");

        // Test adding another keyword
        product.addKeyWord("key2");
        keyWords = product.getKeyWords();
        assertEquals(2, keyWords.size(), "Product should now have two key words");
        assertTrue(keyWords.contains("key2"), "Product should contain key word 'key2'");
    }

    @Test
    public void testRelatedProducts() {
        Product product1 = new Product("Product1", 10.5f, ProductTemperature.AMBIENT, "img/path1");
        Product product2 = new Product("Product2", 15.0f, ProductTemperature.FROZEN, "img/path2");
        Product product3 = new Product("Product3", 20.0f, ProductTemperature.REFRIGERATED, "img/path3");

        // Create RelatedProduct instances for each product pair
        RelatedProduct rp1 = new RelatedProduct(product1, product2, 0.6f);
        RelatedProduct rp2 = new RelatedProduct(product1, product3, 0.8f);
        RelatedProduct rp3 = new RelatedProduct(product2, product3, 0.5f);

        // Test relation values
        assertEquals(0.6f, product1.getRelatedValue(product2), "Relation value between Product1 and Product2 should be 0.6");
        assertEquals(0.8f, product1.getRelatedValue(product3), "Relation value between Product1 and Product3 should be 0.8");
        assertEquals(0.5f, product2.getRelatedValue(product3), "Relation value between Product2 and Product3 should be 0.5");

        // Test modifying a relation value
        product1.setRelatedValue(product2, 0.9f);
        assertEquals(0.9f, product1.getRelatedValue(product2), "Relation value between Product1 and Product2 should be updated to 0.9");

        // Test eliminating a relation (IT'S A PRIVATE METHOD)
        //product1.eraseRelation(product2);
        //assertThrows(IllegalArgumentException.class, () -> product1.getRelatedValue(product2), "After erasing the relation, Product1 should not have a relation with Product2");

        // Test eliminating all relations
        product3.eliminateAllRelations();
        assertThrows(IllegalArgumentException.class, () -> product3.getRelatedValue(product2), "After eliminating all relations, Product3 should not have a relation with Product2");
        assertThrows(IllegalArgumentException.class, () -> product2.getRelatedValue(product3), "After eliminating all relations of Product3, Product2 should not have a relation with it");
    }
}*/