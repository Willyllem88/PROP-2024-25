package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DomainController {
    private static DomainController instance;
    private Supermarket supermarket;
    private Catalog catalog;

    public static DomainController getInstance() {
        if (instance == null) {
            instance = new DomainController();
        }
        return instance;
    }

    private DomainController() {
        supermarket = Supermarket.getInstance();
        catalog = Catalog.getInstance();
    }

    public void logIn(String username, String password) {
        supermarket.logIn(username, password);
    }

    public void closeApp() {

    }

    public void logOut() {
        supermarket.logOut();
    }

    public void importSupermarketConfiguration(String path) {
        supermarket.importSupermarket(path);
    }

    public void exportSupermarketConfiguration(String path) {
        supermarket.exportSupermarket(path);
    }

    public void createSupermarketDistribution(int shelvingUnitsHeight, List<String> temperatures, List<Integer> quantities) {
        if (Objects.isNull(temperatures) || Objects.isNull(quantities) || temperatures.size() != quantities.size())
            throw new IllegalArgumentException("Shelving units definition invalid");
        ArrayList<Pair<ProductTemperature, Integer>> shelvingUnits = new ArrayList<>();
        for (int i = 0; i < temperatures.size(); i++)
            shelvingUnits.add(new Pair<>(ProductTemperature.valueOf(temperatures.get(i)), quantities.get(i)));
        supermarket.createDistribution(shelvingUnitsHeight, shelvingUnits);
    }

    public void sortSupermarketByCatalogProducts(String sortingStrategy) {
        supermarket.setOrderingStrategy(getOrderingStrategy(sortingStrategy));
        supermarket.sortSupermarketCatalog();
    }

    public void sortSupermarketProducts(String sortingStrategy) {
        supermarket.setOrderingStrategy(getOrderingStrategy(sortingStrategy));
        supermarket.sortSupermarketProducts();
    }

    public void addProductToShelvingUnit(String productName, int height, int shelvingUnitPosition) {
        Product product = catalog.getProduct(productName);
        supermarket.addProductToShelvingUnit(height, shelvingUnitPosition, product);
    }

    public void eraseProductFromShelvingUnit(int height, int shelvingUnitPosition) {
        //TODO
    }

    public void swapProductsFromShelvingUnits(int position1, int position2, int height1, int height2) {
        //TODO
    }

    public void modifyShelvingUnitType(int position, String temperatureType) {
        //TODO
    }

    public void addShelvingUnit(int position, String temperature) {
        ProductTemperature productTemperature = ProductTemperature.valueOf(temperature);
        supermarket.addShelvingUnit(position, productTemperature);
    }

    public void eraseShelvingUnit(int position) {
        //TODO
    }

    public void swapShelvingUnits(int position1, int position2) {
        //TODO
    }

    public void emptyShelvingUnits(int position) {
        //TODO
    }

    public void createProduct(String productName, String temperature, float price, String imgPath, List<String>keyWords, List<String> relatedProducts, List<Float> relatedValues) {
        ProductTemperature productTemperature = ProductTemperature.valueOf(temperature);
        List<Product> products = new ArrayList<>();
        for (String relatedProductName: relatedProducts)
            products.add(catalog.getProduct(relatedProductName));
        catalog.createNewProduct(productName, price, productTemperature, imgPath, keyWords, products, relatedValues);
    }

    public void eraseProduct(String productName) {
        catalog.eraseProduct(productName);
    }

    public void modifyProduct(String productName, String temperature, float price, String imagePath, List<String>RealatedKeyWords) {
        Product product = catalog.getProduct(productName);
        ProductTemperature productTemperature = ProductTemperature.valueOf(temperature);
        product.setPrice(price);
        product.setTemperature(productTemperature);
        product.setImgPath(imagePath);
        product.setKeyWords(RealatedKeyWords);
    }

    public void modifyProductRelation(String productName1, String productName2, float realation) {
        Product product1 = catalog.getProduct(productName1);
        Product product2 = catalog.getProduct(productName2);
        catalog.modifyRelationProduct(product1, product2, realation);
    }

    public List<Product> searchProduct(String searchText) {
        return catalog.searchProduct(searchText);
    }

    private OrderingStrategy getOrderingStrategy(String orderingStrategy) {
        return switch (orderingStrategy) {
            case "BruteForce" -> new BruteForce();
            case "Approximation" -> new Approximation();
            default -> throw new IllegalArgumentException("Ordering strategy invalid");
        };
    }
}
