package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public  class ImportFileJSONTest {
    private ImportFileStrategy importFileStrategy;

    @Before
    public void setUp() {
        importFileStrategy = new ImportFileJSON();
    }

    @Test
    public void testImportCatalog() {
        // Path to the test JSON file (make sure the file is in your test resources folder)
        String testFilePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json";

        // Import the catalog using ImportFileJSON
        ArrayList<Product> products = importFileStrategy.importCatalog(testFilePath);

        // Assert that the catalog is not null and contains products
        assertNotNull("The product list should not be null", products);
        assertFalse("The product list should not be empty", products.isEmpty());

        // Check the properties of the first product
        Product firstProduct = products.get(0);
        assertNotNull("The first product name should not be null", firstProduct.getName());
        assertNotNull("The first product price should not be null", firstProduct.getPrice());
        assertNotNull("The first product temperature should not be null", firstProduct.getTemperature());
        assertNotNull("The first product image path should not be null", firstProduct.getImgPath());
        assertNotNull("The first product keyWords should not be null", firstProduct.getKeyWords());

        // Check if the related products have been imported correctly
        assertTrue("The first product should have related products", !firstProduct.getRelatedProducts().isEmpty());
    }
}