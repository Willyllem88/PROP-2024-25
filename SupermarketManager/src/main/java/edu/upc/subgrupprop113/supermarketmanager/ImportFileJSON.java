package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class ImportFileJSON implements ImportFileStrategy{

    @Override
    public Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> importSupermarket(String filePath) {

        return new Pair<ArrayList<Product>, ArrayList<ShelvingUnit>>(new ArrayList<Product>(), new ArrayList<ShelvingUnit>());
    }

    @Override
    public ArrayList<Product> importCatalog(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Product> products = new ArrayList<>();

        try {
            products = objectMapper.readValue(new File(filePath), new TypeReference<ArrayList<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace(); // Puedes manejar la excepción como prefieras
        }

        return products;
    }

    @Override
    public ArrayList<ShelvingUnit> importShelvingUnits(String filePath) {

        return new ArrayList<ShelvingUnit>();
    }


    public static void main(String[] args) throws IOException {
        ImportFileStrategy strategy = new ImportFileJSON();
        ArrayList<Product> products = strategy.importCatalog(".\\dataExample1.json");

        System.out.println("Catálogo de productos:");
        for (Product product : products) {
            System.out.println("Nombre: " + product.getName());
            System.out.println("Precio: " + product.getPrice());
            System.out.println("Temperatura: " + product.getTemperature());
            System.out.println("Imagen: " + product.getImgPath());
            System.out.println("Palabras clave: " + product.getKeyWords());
            System.out.println("---------------------------");
        }
    }
}
