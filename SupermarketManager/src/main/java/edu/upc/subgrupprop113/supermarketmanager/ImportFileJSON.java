package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the ImportFileStrategy interface that imports data from a JSON file.
 */
public class ImportFileJSON implements ImportFileStrategy{

    /**
     * Creates the supermarket data using an import file in JSON format in a path.
     *
     * @param filePath is the path were the file is located.
     * @return the new data imported as a {@link SupermarketData}.
     */
    @Override
    public SupermarketData importSupermarket(String filePath) {
        // Create an ObjectMapper instance to handle the JSON file
        ObjectMapper mapper = new ObjectMapper();
        SupermarketData data = null;

        try {
            // Read the JSON file and map it to CatalogData class
            data = mapper.readValue(new File(filePath), SupermarketData.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Retrieve the list of products from the CatalogData
        Map<String, RelatedProduct> uniqueRelationships = new HashMap<>();
        for (Product product : data.getProducts()) {
            for (RelatedProduct rel : product.getRelatedProducts()) {
                String key = generateRelProdKey(rel.getProduct1(), rel.getProduct2());
                uniqueRelationships.putIfAbsent(key, rel);
            }
            // Remove all previous relations from the product to avoid duplication later
            product.eliminateAllRelations();
        }

        // Add the unique relationships back to both products in each relationship
        for (RelatedProduct relatedProduct : uniqueRelationships.values()) {
            relatedProduct.getProduct1().addRelatedProduct(relatedProduct);
            relatedProduct.getProduct2().addRelatedProduct(relatedProduct);
        }

        // Iterates over the ShelvingUnit distribution and replaces the products in each ShelvingUnit with the
        // corresponding Product from the map, setting it to null if the product name is "None".
        Map<String, Product> productMap = data.getProducts().stream()
                .collect(Collectors.toMap(Product::getName, product -> product));
        for (ShelvingUnit shelvingUnit : data.getDistribution()) {
            for (int i = 0; i < shelvingUnit.getHeight(); ++i) {
                Product prodByName = shelvingUnit.getProduct(i);

                if (prodByName == null) continue;

                Product realProduct = productMap.get(prodByName.getName());
                shelvingUnit.addProduct(realProduct, i);
            }
        }

        return data;
    }

    /**
     * Creates the catalog data using an import file in JSON format in a path.
     *
     * @param filePath is the path were the file is located.
     * @return the new catalog imported as a {@link ArrayList<Product>}.
     */
    @Override
    public ArrayList<Product> importCatalog(String filePath) {

        return new ArrayList<>();
    }

    /**
     * Creates the shelving units data using an import file in JSON format in a path.
     *
     * @param filePath is the path were the file is located.
     * @return the new shelving units imported as a {@link ArrayList<ShelvingUnit>}.
     */
    @Override
    public ArrayList<ShelvingUnit> importShelvingUnits(String filePath) {

        return new ArrayList<ShelvingUnit>();
    }

    /**
     * Creates the new supermarket using import functions.
     * @param args are the arguments to use this function.
     */
    public static void main(String[] args) throws IOException {
        ImportFileStrategy ImportStrategy = new ImportFileJSON();
        String filePath;
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            filePath = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExample1.json";
        else
            filePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json";

        SupermarketData data = ImportStrategy.importSupermarket(filePath);

        data.print();

        Catalog.getInstance().setAllProducts(data.getProducts());
    }

    /**
     * Generates the relation between two products.
     *
     * @return a string saying which one is related with the other one in a lexicographic order to have the unique key satisfied.
     */
    private String generateRelProdKey(Product product1, Product product2) {
        String name1 = product1.getName();
        String name2 = product2.getName();
        return (name1.compareTo(name2) < 0) ? name1 + "-" + name2 : name2 + "-" + name1;
    }
}
