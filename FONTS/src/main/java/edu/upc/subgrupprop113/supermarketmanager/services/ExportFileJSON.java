package edu.upc.subgrupprop113.supermarketmanager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

import java.io.File;

/**
 * Implementation of the ExportFileStrategy interface that exports data from the supermarket to a JSON file.
 */
public class ExportFileJSON implements ExportFileStrategy {

    /**
     * Exports the {@link SupermarketData} to a JSON file at the specified path.
     * @param data the data of the supermarket.
     * @param filePath the path were the JSON file will be located.
     */
    @Override
    public void exportSupermarket(SupermarketData data, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Read the JSON file and map it to CatalogData class
            mapper.writeValue(new File(filePath), data);
        }
        catch (Exception e) {
            throw new RuntimeException("Error exporting supermarket data to JSON file", e);
        }
    }

    /**
     * Main to import a configuration and then to export at a JSON file.
     * @param args all the arguments needed for the execution.
     */
    /*public static void main(String[] args) throws IOException {
        String inputFilePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json";
        String outputFilePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample2.json";
        Supermarket supermarket = Supermarket.getInstance();
        Catalog catalog = Catalog.getInstance();

        supermarket.logIn("admin", "admin");
        supermarket.importSupermarket(inputFilePath);

        SupermarketData data = new SupermarketData();
        data.loadData();
        data.print();

        ExportFileJSON exporter = new ExportFileJSON();
        exporter.exportSupermarket(data, outputFilePath);
    }*/
}
