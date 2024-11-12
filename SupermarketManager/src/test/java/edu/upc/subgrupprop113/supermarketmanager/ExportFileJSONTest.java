package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExportFileJSONTest {
    private Supermarket supermarket;
    private String inputFilePath;
    private String outputFilePath;


    @Before
    public void setup() {
        supermarket = Supermarket.getInstance();

        // Detect the OS and modify the path depending on it.
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            inputFilePath =  "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExample1.json";
            outputFilePath = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExample2.json";
        }
        else {
            inputFilePath =  ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json";
            outputFilePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample2.json";
        }
    }

    @Test
    public void testExportFileJSON() {
        ExportFileStrategy exportFileStrategy = new ExportFileJSON();

        SupermarketData supermarketData = new SupermarketData();

        try {
            supermarket.logIn("admin", "admin");
            supermarket.importSupermarket(inputFilePath);
        }
        catch (Exception e) {
            fail("Either Supermarket::logIn() or Supermarket::importSupermarket(filePath) is not working properly.");
        }

        supermarketData.loadData();

        exportFileStrategy.exportSupermarket(supermarketData, outputFilePath);
    }
}