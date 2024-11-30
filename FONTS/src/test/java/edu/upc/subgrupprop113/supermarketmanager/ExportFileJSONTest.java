package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Supermarket;
import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileStrategy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ExportFileJSONTest {
    private Supermarket supermarket;
    private String inputFilePath;
    private String outputFilePath;


    @Before
    public void setup() {
        supermarket = Supermarket.getInstance();
        supermarket.clear();
        supermarket.logIn("admin", "admin");

        // Detect the OS and modify the path depending on it.
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            inputFilePath =  "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/dataExample1.json";
            outputFilePath = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/dataExample2.json";
        }
        else {
            inputFilePath =  ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\dataExample1.json";
            outputFilePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\dataExample2.json";
        }
    }

    @Test
    public void testExportFileJSON() {
        ExportFileStrategy exportFileStrategy = new ExportFileJSON();

        SupermarketData supermarketData = new SupermarketData();

        try {
            supermarket.importSupermarket(inputFilePath);
        }
        catch (Exception e) {
            fail("Supermarket::importSupermarket(filePath) is not working properly.\n" + e.getMessage());
        }

        supermarketData.loadData();

        exportFileStrategy.exportSupermarket(supermarketData, outputFilePath);

        try {
            Files.deleteIfExists(Path.of(outputFilePath));
        } catch (IOException e) {
            fail("Failed to delete the output file: " + e.getMessage());
        }
    }
}