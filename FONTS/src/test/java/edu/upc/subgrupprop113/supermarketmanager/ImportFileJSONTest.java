package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileStrategy;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public  class ImportFileJSONTest {
    private String filePath;
    private ImportFileStrategy importFileStrategy;

    @Before
    public void setUp() {
        importFileStrategy = new ImportFileJSON();
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            filePath = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/dataExample1.json";
        else
            filePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\dataExample1.json";
    }

    @Test
    public void testImportCatalog() {
        // Import the catalog using ImportFileJSON
        SupermarketData data = importFileStrategy.importSupermarket(filePath);

        // Assert that the catalog is not null and contains products
        assertNotNull("The product list should not be null", data.getProducts());
        assertFalse("The product list should not be empty", data.getProducts().isEmpty());

        // Assert that the distribution is not null
        assertNotNull("The shelving unit list should not be null", data.getDistribution());

        // Assert the shelvingUnitHeight of the supermarket should be grater than one
        assertFalse("The shelving unit height should be greater than 0", data.getShelvingUnitHeight() <= 0);

        // Check the properties of the first product
        Product firstProduct = data.getProducts().getFirst();
        assertNotNull("The first product name should not be null", firstProduct.getName());
        assertNotNull("The first product temperature should not be null", firstProduct.getTemperature());
        assertNotNull("The first product image path should not be null", firstProduct.getImgPath());
        assertNotNull("The first product keyWords should not be null", firstProduct.getKeyWords());

        // Check if the related products have been imported correctly
        assertFalse("The first product should have related products", firstProduct.getRelatedProducts().isEmpty());
    }
}