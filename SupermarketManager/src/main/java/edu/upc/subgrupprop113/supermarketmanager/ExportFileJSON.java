package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ExportFileStrategy interface that exports data from the supermarket to a JSON file.
 */
public class ExportFileJSON implements ExportFileStrategy{
    @Override
    public void exportSupermarket(SupermarketData data, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Read the JSON file and map it to CatalogData class
            mapper.writeValue(new File(filePath), data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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
