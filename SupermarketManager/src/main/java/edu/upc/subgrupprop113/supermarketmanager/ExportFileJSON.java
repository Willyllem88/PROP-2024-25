package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportFileJSON implements ExportFileStrategy{
    @Override
    public void exportSupermarket(SupermarketData data, String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Read the JSON file and map it to CatalogData class
            mapper.writeValue(new File(filePath), this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json";
        Supermarket supermarket = Supermarket.getInstance();
        Catalog catalog = Catalog.getInstance();

        supermarket.logIn("admin", "admin");
        supermarket.importSupermarket(filePath);

        int height = supermarket.getShelvingUnitHeight();
        ArrayList<Product> products = new ArrayList<>(catalog.getAllProducts());
        ArrayList<ShelvingUnit> distribution = new ArrayList<>(supermarket.getShelvingUnits());

        SupermarketData data = new SupermarketData(height, products, distribution);
        data.print();

        ExportFileJSON exporter = new ExportFileJSON();
        exporter.exportSupermarket(data, filePath);

    }
}
