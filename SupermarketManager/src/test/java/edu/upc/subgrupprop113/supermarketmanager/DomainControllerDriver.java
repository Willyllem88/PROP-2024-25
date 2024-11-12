package edu.upc.subgrupprop113.supermarketmanager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DomainControllerDriver {
    public static void main(String[] args) {
        System.out.println("Welcome to Supermarket Manager!\nIf you need help about the commands available enter 'help'");
        boolean main = true;
        System.out.println("Do you want to use predefined test cases? Y/N");
        Scanner driverUsageReader = new Scanner(System.in);
        Scanner commandReader = new Scanner(System.in);
        if (driverUsageReader.next().equalsIgnoreCase("y")) {
            System.out.println("Please, enter the path to the desired test.");
            try {
                FileReader testReader = new FileReader(driverUsageReader.next());
                commandReader = new Scanner(testReader);
            }
            catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        DomainController controller = DomainController.getInstance();

        while (main) {
            if (commandReader.hasNextLine()) {
                String command = commandReader.nextLine();
                switch (command) {
                    case "help":
                        System.out.println(helpperInfo());
                        break;
                    case "closeApp":
                        main = false;
                        break;
                    case "logIn":
                        System.out.println("Please enter your login name:");
                        String username = commandReader.nextLine();
                        System.out.println("Please enter your login password:");
                        String password = commandReader.nextLine();
                        try {
                            controller.logIn(username, password);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "logOut":
                        try {
                            controller.logOut();
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "importSupermarketConfiguration":
                        String filenameImport = commandReader.nextLine();
                        try {
                            controller.importSupermarketConfiguration(filenameImport);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "exportSupermarketConfiguration":
                        String filenameExport = commandReader.nextLine();
                        try {
                            controller.exportSupermarketConfiguration(filenameExport);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "createSupermarketDistribution":
                        int shelvingUnitsHeight = Integer.parseInt(commandReader.nextLine());
                        System.out.println("Number of types of shelving units:");
                        int nbShelvingUnits = Integer.parseInt(commandReader.nextLine());
                        ArrayList<String> temperatures = new ArrayList<>();
                        ArrayList<Integer> quantities = new ArrayList<>();
                        for (int i = 0; i < nbShelvingUnits; i++) {
                            System.out.println("Shelving unit temperature:");
                            String temperature = commandReader.nextLine();
                            temperatures.add(temperature);
                            System.out.println("Quantity:");
                            Integer quantity = Integer.parseInt(commandReader.nextLine());
                            quantities.add(quantity);
                        }
                        try {
                            controller.createSupermarketDistribution(shelvingUnitsHeight, temperatures, quantities);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "sortSupermarketByCatalogProducts":
                        String sortingCatalogStrategy = commandReader.nextLine();
                        try {
                            controller.sortSupermarketByCatalogProducts(sortingCatalogStrategy);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "sortSupermarketProducts":
                        String sortingProductsStrategy = commandReader.nextLine();
                        try {
                            controller.sortSupermarketProducts(sortingProductsStrategy);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "addProductToShelvingUnit":
                        String productName = commandReader.nextLine();
                        int heightAdd = Integer.parseInt(commandReader.nextLine());
                        int shelvingUnitPositionAdd = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.addProductToShelvingUnit(productName, heightAdd, shelvingUnitPositionAdd);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "eraseProductFromShelvingUnit":
                        int heightErase = Integer.parseInt(commandReader.nextLine());
                        int shelvingUnitPositionErase = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.eraseProductFromShelvingUnit(heightErase, shelvingUnitPositionErase);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "swapProductsFromShelvingUnits":
                        int position1, position2, height1, height2;
                        position1 = Integer.parseInt(commandReader.nextLine());
                        position2 = Integer.parseInt(commandReader.nextLine());
                        height1 = Integer.parseInt(commandReader.nextLine());
                        height2 = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.swapProductsFromShelvingUnits(position1, position2, height1, height2);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "modifyShelvingUnitType":
                        int positionModify = Integer.parseInt(commandReader.nextLine());
                        String temperatureTypeModify = commandReader.nextLine();
                        try {
                            controller.modifyShelvingUnitType(positionModify, temperatureTypeModify);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "addShelvingUnit":
                        int positionAdd = Integer.parseInt(commandReader.nextLine());
                        String temperatureTypeAdd = commandReader.nextLine();
                        try {
                            controller.addShelvingUnit(positionAdd, temperatureTypeAdd);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "eraseShelvingUnit":
                        int positionErase = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.eraseShelvingUnit(positionErase);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "swapShelvingUnits":
                        int position1Swap = Integer.parseInt(commandReader.nextLine());
                        int position2Swap = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.swapShelvingUnits(position1Swap, position2Swap);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "emptyShelvingUnits":
                        int positionEmpty = Integer.parseInt(commandReader.nextLine());
                        try {
                            controller.emptyShelvingUnits(positionEmpty);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "createProduct":
                        String name = commandReader.nextLine();
                        String temperatureTypeCreate = commandReader.nextLine();
                        float price = Float.parseFloat(commandReader.nextLine());
                        String imgPath = commandReader.nextLine();
                        System.out.println("Number of keywords:");
                        int nbKeyWords = Integer.parseInt(commandReader.nextLine());
                        ArrayList<String> keyWords = new ArrayList<>();
                        for (int i = 0; i < nbKeyWords; i++) {
                            System.out.println("Key word " + i + ":");
                            String keyWord = commandReader.nextLine();
                            keyWords.add(keyWord);
                        }
                        System.out.println("Number of related products:");
                        int nbRelatedProducts = Integer.parseInt(commandReader.nextLine());
                        ArrayList<String> relatedProducts = new ArrayList<>();
                        ArrayList<Float> relatedValues = new ArrayList<>();
                        for (int i = 0; i < nbRelatedProducts; i++) {
                            System.out.println("Product name " + i + ":");
                            String relatedProductName = commandReader.nextLine();
                            relatedProducts.add(relatedProductName);
                            System.out.println("Product relation " + i + ":");
                            float relatedValue = Float.parseFloat(commandReader.nextLine());
                            relatedValues.add(relatedValue);
                        }
                        try {
                            controller.createProduct(name, temperatureTypeCreate, price, imgPath, keyWords, relatedProducts, relatedValues);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "eraseProduct":
                        String productNameErase = commandReader.nextLine();
                        try {
                            controller.eraseProduct(productNameErase);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "modifyProduct":
                        String nameModify = commandReader.nextLine();
                        String temperatureProductModify = commandReader.nextLine();
                        float priceModify = Float.parseFloat(commandReader.nextLine());
                        String imgPathModify = commandReader.nextLine();
                        System.out.println("Number of keywords:");
                        int nbKeyWordsModify = Integer.parseInt(commandReader.nextLine());
                        ArrayList<String> keyWordsModify = new ArrayList<>();
                        for (int i = 0; i < nbKeyWordsModify; i++) {
                            System.out.println("Key word " + i + ":");
                            String keyWord = commandReader.nextLine();
                            keyWordsModify.add(keyWord);
                        }
                        try {
                            controller.modifyProduct(nameModify, temperatureProductModify, priceModify, imgPathModify, keyWordsModify);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "modifyProductRelation":
                        String productName1, productName2;
                        productName1 = commandReader.nextLine();
                        productName2 = commandReader.nextLine();
                        float realtion = Float.parseFloat(commandReader.nextLine());
                        try {
                            controller.modifyProductRelation(productName1, productName2, realtion);
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "searchProduct":
                        String searchText = commandReader.nextLine();
                        List<Product> products;
                        try {
                            products = controller.searchProduct(searchText);
                            System.out.println("Search Result:");
                            System.out.println("=======================================");
                            for (Product product : products) {
                                System.out.println(product.getInfo());
                            }
                            System.out.println("=======================================");
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    case "getSupermarketInfo":
                        System.out.print(controller.getSupermarketInfo());
                        break;
                    case "getCatalogInfo":
                        System.out.print(controller.getCatalogInfo());
                        break;
                    case "getShelvingUnitInfo":
                        int position = Integer.parseInt(commandReader.nextLine());
                        try {
                            System.out.print(controller.getShelvingUnitInfo(position));
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "getProductInfo":
                        String productNameInfo = commandReader.nextLine();
                        try {
                            System.out.print(controller.getProductInfo(productNameInfo));
                        }
                        catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid command");
                }
            }
        }
    }

    /**
     * Provides information on all available commands in the system.
     *
     * This method returns a string that describes all the commands that the user
     * can input to interact with the system. It includes a brief explanation of
     * what each command does.
     *
     * @return A string containing a list of commands and their descriptions.
     */
    private static String helpperInfo() {
        return
                "Available Commands:\n" +
                        "=======================================\n" +
                        "help - Displays this help message.\n" +
                        "closeApp - Closes the application.\n" +
                        "logIn - Logs the user in. (Requires username and password)\n" +
                        "logOut - Logs the user out.\n" +
                        "importSupermarketConfiguration - Imports supermarket configuration from a file.\n" +
                        "exportSupermarketConfiguration - Exports supermarket configuration to a file.\n" +
                        "createSupermarketDistribution - Creates a new supermarket distribution.\n" +
                        "sortSupermarketByCatalogProducts - Sorts the supermarket by catalog products based on the specified strategy.\n" +
                        "The strategies can be:\n" +
                        "BruteForce\n"+
                        "Approximation\n"+
                        "sortSupermarketProducts - Sorts supermarket products based on the specified strategy.\n" +
                        "The strategies can be:\n" +
                        "BruteForce\n"+
                        "Approximation\n"+
                        "addProductToShelvingUnit - Adds a product to a shelving unit.\n" +
                        "eraseProductFromShelvingUnit - Removes a product from a shelving unit.\n" +
                        "swapProductsFromShelvingUnits - Swaps products between two shelving units.\n" +
                        "modifyShelvingUnitType - Modifies the type (temperature) of a shelving unit.\n" +
                        "The types can be:\n" +
                        "FROZEN\n"+
                        "REFRIGERATED\n"+
                        "AMBIENT\n" +
                        "addShelvingUnit - Adds a new shelving unit.\n" +
                        "eraseShelvingUnit - Removes a shelving unit.\n" +
                        "swapShelvingUnits - Swaps two shelving units.\n" +
                        "emptyShelvingUnits - Empties the contents of a shelving unit.\n" +
                        "createProduct - Creates a new product and adds it to the catalog.\n" +
                        "eraseProduct - Erases a product from the catalog.\n" +
                        "modifyProduct - Modifies an existing product in the catalog.\n" +
                        "modifyProductRelation - Modifies the relation between two products.\n" +
                        "searchProduct - Searches for products in the catalog based on a search text.\n" +
                        "getSupermarketInfo - Retrieves information about the supermarket.\n" +
                        "getCatalogInfo - Retrieves information about the catalog.\n" +
                        "getShelvingUnitInfo - Retrieves information about a shelving unit.\n" +
                        "getProductInfo - Retrieves information about a specific product.\n" +
                        "=======================================";
    }

}
