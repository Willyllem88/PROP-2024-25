package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Catalog;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class CatalogTest {
    private Catalog catalog;
    private Product product1, product2, product3;

    @Before
    public void setUp() {
        // Initialize the catalog
        catalog = Catalog.getInstance();
        catalog.clear();

        // Create keywords and relation values
        List<String> keywords1 = Arrays.asList("fruit", "apple", "sweet");
        List<String> keywords2 = Arrays.asList("snack", "chocolate", "sweet");
        List<String> keywords3 = Arrays.asList("drink", "water", "refreshing");

        // Create related products
        List<Product> relatedProducts1 = new ArrayList<>();
        List<Product> relatedProducts2 = new ArrayList<>();
        List<Product> relatedProducts3 = new ArrayList<>();

        List<Float> relatedValues1 = new ArrayList<>();
        List<Float> relatedValues2 = new ArrayList<>();
        List<Float> relatedValues3 = new ArrayList<>();

        // Create products and add them to the catalog
        product1 = catalog.createNewProduct("Apple", 1.99f, ProductTemperature.AMBIENT, "/images/apple.jpg", keywords1, relatedProducts1, relatedValues1);
        relatedProducts2.add(product1);
        relatedValues2.add(0.2f);
        product2 = catalog.createNewProduct("Chocolate Bar", 2.50f, ProductTemperature.AMBIENT, "/images/chocolate.jpg", keywords2, relatedProducts2, relatedValues2);
        relatedProducts3.add(product1);
        relatedProducts3.add(product2);
        relatedValues3.add(0.5f);
        relatedValues3.add(0.4f);
        product3 = catalog.createNewProduct("Water Bottle", 1.00f, ProductTemperature.AMBIENT, "/images/water.jpg", keywords3, relatedProducts3, relatedValues3);
    }

    @Test
    public void testGetProduct() {
        // Test getting an existing product
        Product retrievedProduct = catalog.getProduct("Apple");
        assertEquals("Should retrieve the correct product by name.", product1, retrievedProduct);

        // Test getting a non-existing product
        try {
            catalog.getProduct("Milk");
            fail("Expected an IllegalArgumentException to be thrown when attempting to retrieve a product that does not exist in the catalog.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetAllProductsReturnsImmutableList() {
        List<Product> products = catalog.getAllProducts();

        // Check that the returned list contains the products added
        assertEquals("The number of products should be 3.", 3, products.size());
        assertTrue("The list should contain product1.", products.contains(product1));
        assertTrue("The list should contain product2.", products.contains(product2));
        assertTrue("The list should contain product3.", products.contains(product3));

        // Verify that attempting to modify the list throws an UnsupportedOperationException
        try {
            products.add(new Product("Orange", 0.99f, ProductTemperature.AMBIENT, "/images/orange.jpg"));
            fail("Attempting to modify the products list should throw an UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testContainsByName() {
        // Test contains method for an existing product
        assertTrue("Catalog should contain 'Apple'.", catalog.contains("Apple"));

        // Test contains method for a non-existing product
        assertFalse("Catalog should not contain 'Banana'.", catalog.contains("Banana"));
    }

    @Test
    public void testContainsByProduct() {
        // Test contains method for an existing product object
        assertTrue("Catalog should contain the product 'Apple'.", catalog.contains(product1));

        // Test contains method for a non-existing product object
        Product nonExistentProduct = new Product("Banana", 0.99f, ProductTemperature.AMBIENT, "/images/banana.jpg");
        assertFalse("Catalog should not contain the product 'Banana'.", catalog.contains(nonExistentProduct));
    }

    @Test
    public void testSetAllProducts1() {
        catalog.clear();

        Product p1, p2, p3;
        p1 = new Product("Pineapple", 3.99f, ProductTemperature.AMBIENT, "/images/pineapple.jpg");
        p2 = new Product("ChewingGum", 1.00f, ProductTemperature.AMBIENT, "/images/chewingGum.jpg");
        p3 = new Product("Ice", 7.99f, ProductTemperature.FROZEN, "/images/ice.jpg");

        // Null products list - should throw IllegalArgumentException
        try {
            catalog.setAllProducts(null);
            fail("Expected IllegalArgumentException for null products list.");
        } catch (IllegalArgumentException e) { }

        // Products list with insufficient relations (two products, one relation needed)
        try {
            catalog.setAllProducts(Arrays.asList(p1, p2));
            fail("Expected IllegalStateException due to insufficient unique relations.");
        } catch (IllegalArgumentException e) { }

        RelatedProduct rel1 = new RelatedProduct(p1, p2, 0.2f);

        try {
            catalog.setAllProducts(Arrays.asList(p1, p2));
        } catch (Exception e) {
            fail("Expected no exception for valid products list with correct relations.");
        }

        // Products list with duplicates - should throw IllegalArgumentException
        try {
            catalog.setAllProducts(Arrays.asList(p1, p2, p2));
            fail("Expected IllegalArgumentException for duplicate products.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        RelatedProduct rel2 = new RelatedProduct(p1, p3, 0.4f);

        // Referenced products not in the list
        try {
            catalog.setAllProducts(Arrays.asList(p1, p2));
            fail("Expected an exception for referencing products that are not in the list.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetAllProducts2() {
        catalog.clear();
        Product p1, p2, p3;
        p1 = new Product("Pineapple", 3.99f, ProductTemperature.AMBIENT, "/images/pineapple.jpg");
        p2 = new Product("ChewingGum", 1.00f, ProductTemperature.AMBIENT, "/images/chewingGum.jpg");
        p3 = new Product("Ice", 7.99f, ProductTemperature.FROZEN, "/images/ice.jpg");

        RelatedProduct rel1 = new RelatedProduct(p1, p2, 0.2f);

        // Referenced products not in the list
        try {
            catalog.setAllProducts(Arrays.asList(p1, p3));
            fail("Expected an exception for referencing products that are not in the list.");
        } catch (IllegalArgumentException e) { }

        RelatedProduct rel2 = new RelatedProduct(p1, p3, 0.4f);

        // Fewer number of relations than required in the products list
        try {
            catalog.setAllProducts(Arrays.asList(p1, p2, p3));
            fail("Expected an exception for fewer number of relations than required.");
        } catch (IllegalArgumentException e) { }

        RelatedProduct rel3 = new RelatedProduct(p2, p3, 0.6f);

        // Number relations exceeds the required number in the products list
        try {
            catalog.setAllProducts(Arrays.asList(p1, p2));
            fail("Expected an exception for exceeding the number of relations.");
        } catch (IllegalArgumentException e) { }

        try {
            catalog.setAllProducts(Arrays.asList(p1, p2, p3));
        } catch (Exception e) {
            fail("Expected no exception for valid products list with correct relations.");
        }
    }

    @Test
    public void testCreateNewProductSuccessfully() {
        // Test that creating a new product works as expected
        List<String> keywords = Arrays.asList("bread", "wheat", "baked");
        final List<Product> relatedProducts = Arrays.asList(product1, product2, product3);
        final List<Float> relatedValues = Arrays.asList(0.3f, 0.5f, 0.2f);

        // Create new product successfully
        Product bread = catalog.createNewProduct("Bread", 1.50f, ProductTemperature.AMBIENT, "/images/bread.jpg", keywords, relatedProducts, relatedValues);
        assertEquals("The product name should be 'Bread'.", "Bread", bread.getName());
        assertTrue("The catalog should contain the product 'Bread'.", catalog.contains("Bread"));

        // Product name already exists
        try {
            catalog.createNewProduct("Apple", 1.20f, ProductTemperature.AMBIENT, "/images/banana.jpg", keywords, relatedProducts, relatedValues);
            fail("Creating a product with an existing name 'Apple' should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) { }

        // Incorrect relatedProducts size
        final List<Product> auxRelatedProducts = Arrays.asList(product1, product2);
        try {
            catalog.createNewProduct("Grapes", 2.50f, ProductTemperature.AMBIENT, "/images/grapes.jpg", keywords, auxRelatedProducts, relatedValues);
            fail("Creating a product with incorrect relatedProducts size should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) { }

        // Incorrect relatedValues size
        final List<Float> auxRelatedValues = Arrays.asList(0.3f, 0.5f);
        try {
            catalog.createNewProduct("Strawberry", 1.75f, ProductTemperature.AMBIENT, "/images/strawberry.jpg", keywords, relatedProducts, auxRelatedValues);
            fail("Creating a product with incorrect relatedValues size should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testEraseProduct() {
        //Erase product 1
        catalog.eraseProduct("Apple");

        assertFalse("Catalog should not contain 'Apple'.", catalog.contains("Apple"));

        try {
            product1.getRelatedValue(product2);
            fail("product1 (Apple) should not be related to product2 (Chocolate Bar) before erasing the product from the catalog");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testModifyRelationProduct() {
        catalog.modifyRelationProduct(product1, product2, 0.9f);

        assertEquals("Product1's relation with product'2' should be updated to 0.9.", 0.9f, product1.getRelatedValue(product2), 0.01f);
        assertEquals("Product2's relation with product'1' should be updated to 0.9.", 0.9f, product2.getRelatedValue(product1), 0.01f);
    }

    @Test
    public void searchProduct() {
        List<Product> res1 = catalog.searchProduct("appl"); //Like apple
        List<Product> res2 = catalog.searchProduct("sweet"); //Like refreshing

        assertEquals("When searching 'appl' the first result should be Apple", "Apple", res1.get(0).getName());
        assertEquals("When searching 'refrhing' the first result should be Water Bottle", "Apple", res2.get(0).getName());
    }
}
