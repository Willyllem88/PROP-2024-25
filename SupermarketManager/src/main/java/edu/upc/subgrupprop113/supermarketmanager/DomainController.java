package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

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

    }

    public void exportSupermarketConfiguration(String path) {

    }

    public void createSupermarketDistribution(int shelvingUnitsHeight, List<String> temperatures, List<String> quantities) {

    }

    public void sortSupermarketByCatalogProducts(String sortingStrategy) {

    }

    public void sortSupermarketProducts(String sortingStrategy) {

    }

    public void addProductToShelvingUnit(String productId, int height, int shelvingUnitPosition) {

    }

    public void eraseProductFromShelvingUnit(int height, int shelvingUnitPosition) {

    }

    public void swapProductsFromShelvingUnits(int position1, int position2, int height1, int height2) {

    }

    public void modifyShelvingUnitType(int position, String temperatureType) {

    }

    public void addShelvingUnit(int position, String temperature) {
        
    }

    public void eraseShelvingUnit(int position) {

    }

    public void swapShelvingUnits(int position1, int position2) {

    }

    public void emptyShelvingUnits(int position) {

    }

    public void createProduct(String productName, String temperature, float price, String imagePath, List<String>RealatedKeyWords) {

    }

    public void eraseProduct(String productId) {

    }

    public void modifyProduct(String productName, String temperature, float price, String imagePath, List<String>RealatedKeyWords) {

    }

    public void modifyProductSimilarity(String productId1, String productId2, float similarity) {

    }

    public void searchProduct(String searchText) {
        
    }

}
