package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class CatalogTest {
    private Catalog catalog;

    private Product product1, product2, product3;

    @BeforeEach
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
        assertEquals(product1, retrievedProduct, "Should retrieve the correct product by name.");

        // Test getting a non-existing product
        assertThrows(IllegalArgumentException.class, () -> catalog.getProduct("Milk"), "Expected an IllegalArgumentException to be thrown when attempting to retrieve a product that does not exist in the catalog.");
    }

    @Test
    public void testGetAllProductsReturnsImmutableList() {
        List<Product> products = catalog.getAllProducts();

        // Check that the returned list contains the products added
        assertEquals(3, products.size(), "The number of products should be 3.");
        assertTrue(products.contains(product1), "The list should contain product1.");
        assertTrue(products.contains(product2), "The list should contain product2.");
        assertTrue(products.contains(product3), "The list should contain product3.");

        // Verify that attempting to modify the list throws an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class,
                () -> products.add(new Product("Orange", 0.99f, ProductTemperature.AMBIENT, "/images/orange.jpg")),
                "Attempting to modify the products list should throw an UnsupportedOperationException.");
    }

    @Test
    public void testContainsByName() {
        // Test contains method for an existing product
        assertTrue(catalog.contains("Apple"), "Catalog should contain 'Apple'.");

        // Test contains method for a non-existing product
        assertFalse(catalog.contains("Banana"), "Catalog should not contain 'Banana'.");
    }

    @Test
    public void testContainsByProduct() {
        // Test contains method for an existing product object
        assertTrue(catalog.contains(product1), "Catalog should contain the product 'Apple'.");

        // Test contains method for a non-existing product object
        Product nonExistentProduct = new Product("Banana", 0.99f, ProductTemperature.AMBIENT, "/images/banana.jpg");
        assertFalse(catalog.contains(nonExistentProduct),
                "Catalog should not contain the product 'Banana'.");
    }

    @Test
    public void testSetAllProducts1() {
        catalog.clear();

        Product p1, p2, p3;
        p1 = new Product("Pineapple", 3.99f, ProductTemperature.AMBIENT, "/images/pineapple.jpg");
        p2 = new Product("ChewingGum", 1.00f, ProductTemperature.AMBIENT, "/images/chewingGum.jpg");
        p3 = new Product("Ice", 7.99f, ProductTemperature.FROZEN, "/images/ice.jpg");

        // Null products list - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(null),
                "Expected IllegalArgumentException for null products list.");

        // Products list with insufficient relations (two products, one relation needed)
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p2)),
                "Expected IllegalStateException due to insufficient unique relations.");

        RelatedProduct rel1 = new RelatedProduct(p1, p2, 0.2f);

        assertDoesNotThrow(() -> catalog.setAllProducts(Arrays.asList(p1, p2)),
                "Expected no exception for valid products list with correct relations.");

        // Products list with duplicates - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p2, p2)),
                "Expected IllegalArgumentException for duplicate products.");

        RelatedProduct rel2 = new RelatedProduct(p1, p3, 0.4f);

        // Referenced products not in the list
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p2)),
                "Expected an exception for referencing products that are not in the list.");
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
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p3)),
                "Expected an exception for referencing products that are not in the list.");

        RelatedProduct rel2 = new RelatedProduct(p1, p3, 0.4f);

        // Fewer number of relations than required in the products list
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p2, p3)),
                "Expected an exception for fewer number of relations than required.");

        RelatedProduct rel3 = new RelatedProduct(p2, p3, 0.6f);

        // Number relations exceeds the required number in the products list
        assertThrows(IllegalArgumentException.class, () -> catalog.setAllProducts(Arrays.asList(p1, p2)),
                "Expected an exception for exceeding the number of relations.");

        assertDoesNotThrow(() -> catalog.setAllProducts(Arrays.asList(p1, p2, p3)),
                "Expected no exception for valid products list with correct relations.");
    }

    @Test
    public void testCreateNewProductSuccessfully() {
        // Test that creating a new product works as expected
        List<String> keywords = Arrays.asList("bread", "wheat", "baked");
        final List<Product> relatedProducts = Arrays.asList(product1, product2, product3);
        final List<Float> relatedValues = Arrays.asList(0.3f, 0.5f, 0.2f);

        // Create new product successfully
        Product bread = catalog.createNewProduct("Bread", 1.50f, ProductTemperature.AMBIENT, "/images/bread.jpg", keywords, relatedProducts, relatedValues);
        assertEquals("Bread", bread.getName(), "The product name should be 'Bread'.");
        assertTrue(catalog.contains("Bread"), "The catalog should contain the product 'Bread'.");

        // Product name already exists
        assertThrows(IllegalArgumentException.class, () -> catalog.createNewProduct("Apple", 1.20f, ProductTemperature.AMBIENT, "/images/banana.jpg", keywords, relatedProducts, relatedValues),
                "Creating a product with an existing name 'Apple' should throw an IllegalArgumentException.");

        // Incorrect relatedProducts size
        final List<Product> auxRelatedProducts = Arrays.asList(product1, product2);
        assertThrows(IllegalArgumentException.class, () -> catalog.createNewProduct("Grapes", 2.50f, ProductTemperature.AMBIENT, "/images/grapes.jpg", keywords, auxRelatedProducts, relatedValues),
                "Creating a product with incorrect relatedProducts size should throw an IllegalArgumentException.");

        // Incorrect relatedValues size
        final List<Float> auxRelatedValues = Arrays.asList(0.3f, 0.5f);
        assertThrows(IllegalArgumentException.class, () -> catalog.createNewProduct("Strawberry", 1.75f, ProductTemperature.AMBIENT, "/images/strawberry.jpg", keywords, relatedProducts, auxRelatedValues),
                "Creating a product with incorrect relatedValues size should throw an IllegalArgumentException.");
    }

    @Test
    void testEraseProduct() {
        //Erase product 1
        catalog.eraseProduct("Apple");

        assertFalse(catalog.contains("Apple"), "Catalog should not contain 'Apple'.");

        assertThrows(IllegalArgumentException.class, () -> product1.getRelatedValue(product2),
                "product1 (Apple) should not be related to product2 (Chocolate Bar) before erasing the product from the catalog");

    }

    @Test
    void testModifyRelationProduct() {
        catalog.modifyRelationProduct(product1, product2, 0.9f);

        assertEquals(0.9f, product1.getRelatedValue(product2),
                "Product1's relation with product'2' should be updated to 0.9.");
        assertEquals(0.9f, product2.getRelatedValue(product1),
                "Product2's relation with product'1' should be updated to 0.9.");
    }

    @Test
    void searchProduct() {
        List<Product> res1 = catalog.searchProduct("appl"); //Like apple
        List<Product> res2 = catalog.searchProduct("refrhing"); //Like refreshing, a water bottle keyword

        assertEquals("Apple", res1.getFirst().getName(),
                "When searching 'appl' he first result should be Apple");
        assertEquals("Water Bottle", res2.getFirst().getName(),
                "When searching 'refrhing' he first result should be Water Bottle");
    }
}
