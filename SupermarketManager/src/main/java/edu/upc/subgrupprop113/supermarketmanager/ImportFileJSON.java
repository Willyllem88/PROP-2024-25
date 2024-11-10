package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//TODO: add supermarket height, all shelving units should respect that.

/**
 * Implementation of the ImportFileStrategy interface that imports data from a JSON file.
 */
public class ImportFileJSON implements ImportFileStrategy{
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
            for (Product product : shelvingUnit.getProducts()) {
                if (product.getName().equals("None")) product = null;
                else product = productMap.get(product.getName());
            }
        }

        return data;
    }

    @Override
    public ArrayList<Product> importCatalog(String filePath) {

        return new ArrayList<>();
    }

    @Override
    public ArrayList<ShelvingUnit> importShelvingUnits(String filePath) {

        return new ArrayList<ShelvingUnit>();
    }


    public static void main(String[] args) throws IOException {
        ImportFileStrategy ImportStrategy = new ImportFileJSON();
        SupermarketData data = ImportStrategy.importSupermarket(".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExample1.json");

        /*for (Product product : data.getProducts()) {
            System.out.println("    Nombre: " + product.getName());
            System.out.println("    Precio: " + product.getPrice());
            System.out.println("    Temperatura: " + product.getTemperature());
            System.out.println("    Imagen: " + product.getImgPath());
            System.out.println("    Palabras clave: " + product.getKeyWords());
            System.out.print("          Values: ");
            for (RelatedProduct relatedProduct : product.getRelatedProducts()) {
                System.out.print(relatedProduct.getValue() + " ");
            }
            System.out.println();
            System.out.println("    ---------------------------");
        }*/

        data.print();

        Catalog.getInstance().setAllProducts(data.getProducts());
    }

    private String generateRelProdKey(Product product1, Product product2) {
        String name1 = product1.getName();
        String name2 = product2.getName();
        // Orden lexicográfico para la clave única
        return (name1.compareTo(name2) < 0) ? name1 + "-" + name2 : name2 + "-" + name1;
    }
}



/*System.out.println("UNIQUE RELATIONS: " + uniqueRelationships.size());
        System.out.println();
        System.out.println("SHELVING UNITS: " + uniqueRelationships.size());
        System.out.println("----------------------------------");
        for (ShelvingUnit shelvingUnit : data.getDistribution()) {
            System.out.println("uid: " + shelvingUnit.getUid());
            System.out.println("temperature: " + shelvingUnit.getTemperature());
            System.out.println("prodsize: " + shelvingUnit.getProducts().size());
            System.out.print("products: ");
            for (Product product : products) {
                System.out.println("    Nombre: " + product.getName());
                System.out.println("    Precio: " + product.getPrice());
                System.out.println("    Temperatura: " + product.getTemperature());
                System.out.println("    Imagen: " + product.getImgPath());
                System.out.println("    Palabras clave: " + product.getKeyWords());
                System.out.print("          Values: ");
                for (RelatedProduct relatedProduct : product.getRelatedProducts()) {
                    System.out.print(relatedProduct.getValue() + " ");
                }
                System.out.println();
                System.out.println("    ---------------------------");
            }
            System.out.println();
            System.out.println("----------------------------------");
        }*/
