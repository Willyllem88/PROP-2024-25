package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javafx.util.Pair;

/**
 * Implementation of the ImportFileStrategy interface that imports data from a JSON file.
 * The JSON data is parsed into a list of {@link Product} objects and their related products.
 */
public class ImportFileJSON implements ImportFileStrategy{
    /**
     * Helper class to represent the structure of the JSON file.
     * It contains a list of {@link Product} objects.
     */
    public static class CatalogData {
        private ArrayList<Product> products;

        public CatalogData() {
            products = new ArrayList<>();
        }

        public ArrayList<Product> getProducts() { return products; }
        public void setProducts(ArrayList<Product> products) { this.products = products; }
    }

    @Override
    public Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> importSupermarket(String filePath) {

        return new Pair<ArrayList<Product>, ArrayList<ShelvingUnit>>(new ArrayList<Product>(), new ArrayList<ShelvingUnit>());
    }

    @Override
    public ArrayList<Product> importCatalog(String filePath) {
        // Create an ObjectMapper instance to handle the JSON file
        ObjectMapper mapper = new ObjectMapper();
        CatalogData data = null;
        try {
            // Read the JSON file and map it to CatalogData class
            data = mapper.readValue(new File(filePath), CatalogData.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Retrieve the list of products from the CatalogData
        List<Product> products = data.getProducts();

        // Retrieve the list of products from the CatalogData
        Map<String, RelatedProduct> uniqueRelationships = new HashMap<>();
        for (Product product : products) {
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

        System.out.println("UNIQUE RELATIONS: " + uniqueRelationships.size());

        return new ArrayList<>(products);
    }

    @Override
    public ArrayList<ShelvingUnit> importShelvingUnits(String filePath) {

        return new ArrayList<ShelvingUnit>();
    }


    public static void main(String[] args) throws IOException {
        ImportFileStrategy ImportStrategy = new ImportFileJSON();
        ArrayList<Product> products = ImportStrategy.importCatalog(".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json");

        System.out.println("Catálogo de productos:");
        for (Product product : products) {
            System.out.println("Nombre: " + product.getName());
            System.out.println("Precio: " + product.getPrice());
            System.out.println("Temperatura: " + product.getTemperature());
            System.out.println("Imagen: " + product.getImgPath());
            System.out.println("Palabras clave: " + product.getKeyWords());
            System.out.print("    Values: ");
            for (RelatedProduct relatedProduct : product.getRelatedProducts()) {
                System.out.print(relatedProduct.getValue() + " ");
            }
            System.out.println();
            System.out.println("---------------------------");
        }

        Set<RelatedProduct> uniqueRelations = new HashSet<>();
        for (Product product : products) {
            uniqueRelations.addAll(product.getRelatedProducts());
        }
        System.out.println("UNIQUE RELATIONS: " + uniqueRelations.size());

        Catalog.getInstance().setAllProducts(products);
    }

    private String generateRelProdKey(Product product1, Product product2) {
        String name1 = product1.getName();
        String name2 = product2.getName();
        // Orden lexicográfico para la clave única
        return (name1.compareTo(name2) < 0) ? name1 + "-" + name2 : name2 + "-" + name1;
    }
}
