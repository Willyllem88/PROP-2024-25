package edu.upc.subgrupprop113.supermarketmanager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DomainControllerDriver {

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Emojis UTF-8
    public static final String SUCCESS_EMOJI = "✅"; // White Heavy Check Mark
    public static final String ERROR_EMOJI = "❌";   // Cross Mark
    public static final String PROMPT_EMOJI = "➡";  // Black Rightwards Arrow
    public static final String SHOPPING_CART_EMOJI = "\uD83D\uDED2"; // Shopping Cart

    public static void main(String[] args) {
        System.out.println(CYAN + "Welcome to Supermarket Manager! " + SHOPPING_CART_EMOJI);
        System.out.println(WHITE + "If you need help about the commands available, enter 'help'" + RESET);
        System.out.print("\n" + PURPLE + PROMPT_EMOJI + " Do you want to use predefined test cases? Y/N: ");

        Scanner driverUsageReader = new Scanner(System.in);
        Scanner commandReader = new Scanner(System.in);

        PrintStream normalOutput = System.out;
        PrintStream errOutput = System.err;

        if (driverUsageReader.nextLine().equalsIgnoreCase("y")) {
            System.out.print(PURPLE + PROMPT_EMOJI + " Please, enter the path to the desired test: ");
            try {
                FileReader testReader = new FileReader(driverUsageReader.nextLine());
                commandReader = new Scanner(testReader);
                System.out.print(PURPLE + PROMPT_EMOJI + " Please, enter the path to the desired output file: ");
                String outputFilePath = driverUsageReader.nextLine();
                normalOutput = new PrintStream(new FileOutputStream(outputFilePath + "_output.txt"));
                errOutput = new PrintStream(new FileOutputStream(outputFilePath + "_error.txt"));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        DomainController controller = DomainController.getInstance();
        boolean main = true;
        System.out.println();
        while (main) {
            System.out.print(BLUE + PROMPT_EMOJI + " ");
            try {
                if (commandReader.hasNextLine()) {
                    String command = commandReader.nextLine();
                    switch (command) {
                        case "help":
                            normalOutput.println(helperInfo());
                            break;
                        case "closeApp":
                            main = false;
                            break;
                        case "logIn":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter your login name (String): ");
                            String username = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter your login password (String): ");
                            String password = commandReader.nextLine();
                            controller.logIn(username, password);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Logged in successfully!" + RESET);
                            break;
                        case "logOut":
                            controller.logOut();
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Logged out successfully!" + RESET);
                            break;
                        case "importSupermarketConfiguration":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the filename to import (String): ");
                            String filenameImport = commandReader.nextLine();
                            controller.importSupermarketConfiguration(filenameImport);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Imported successfully!" + RESET);
                            break;
                        case "exportSupermarketConfiguration":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the filename to export (String): ");
                            String filenameExport = commandReader.nextLine();
                            controller.exportSupermarketConfiguration(filenameExport);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Exported successfully!" + RESET);
                            break;
                        case "createSupermarketDistribution":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Shelving units height (integer): ");
                            int shelvingUnitsHeight = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Number of types of shelving units (integer): ");
                            int nbShelvingUnits = Integer.parseInt(commandReader.nextLine());
                            ArrayList<String> temperatures = new ArrayList<>();
                            ArrayList<Integer> quantities = new ArrayList<>();
                            for (int i = 0; i < nbShelvingUnits; i++) {
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Shelving unit temperature (FROZEN, REFRIGERATED, AMBIENT): ");
                                String temperature = commandReader.nextLine();
                                temperatures.add(temperature);
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Quantity (integer): ");
                                Integer quantity = Integer.parseInt(commandReader.nextLine());
                                quantities.add(quantity);
                            }
                            controller.createSupermarketDistribution(shelvingUnitsHeight, temperatures, quantities);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Distribution created successfully!" + RESET);
                            break;
                        case "sortSupermarketByCatalogProducts":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the sorting strategy (BruteForce, Approximation): ");
                            String sortingCatalogStrategy = commandReader.nextLine();
                            controller.sortSupermarketByCatalogProducts(sortingCatalogStrategy);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Ordered successfully!" + RESET);
                            break;
                        case "sortSupermarketProducts":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the sorting strategy (BruteForce, GreedyBacktracking, Approximation): ");
                            String sortingProductsStrategy = commandReader.nextLine();
                            controller.sortSupermarketProducts(sortingProductsStrategy);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Ordered successfully!" + RESET);
                            break;
                        case "addProductToShelvingUnit":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the product name (String): ");
                            String productName = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the height (integer): ");
                            int heightAdd = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the shelving unit position (integer): ");
                            int shelvingUnitPositionAdd = Integer.parseInt(commandReader.nextLine());
                            controller.addProductToShelvingUnit(productName, heightAdd, shelvingUnitPositionAdd);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Added successfully!" + RESET);
                            break;
                        case "removeProductFromShelvingUnit":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the height (integer): ");
                            int heightErase = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the shelving unit position (integer): ");
                            int shelvingUnitPositionErase = Integer.parseInt(commandReader.nextLine());
                            controller.removeProductFromShelvingUnit(heightErase, shelvingUnitPositionErase);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Removed successfully!" + RESET);
                            break;
                        case "swapProductsFromShelvingUnits":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the first shelving unit position (integer): ");
                            int position1 = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the second shelving unit position (integer): ");
                            int position2 = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the height in the first shelving unit (integer): ");
                            int height1 = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the height in the second shelving unit (integer): ");
                            int height2 = Integer.parseInt(commandReader.nextLine());
                            controller.swapProductsFromShelvingUnits(position1, position2, height1, height2);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Swapped successfully!" + RESET);
                            break;
                        case "modifyShelvingUnitType":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the shelving unit position (integer): ");
                            int positionModify = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the new temperature type (FROZEN, REFRIGERATED, AMBIENT): ");
                            String temperatureTypeModify = commandReader.nextLine();
                            controller.modifyShelvingUnitType(positionModify, temperatureTypeModify);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Modified successfully!" + RESET);
                            break;
                    case "addShelvingUnit":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the position to add the shelving unit (integer): ");
                            int positionAdd = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the temperature type (FROZEN, REFRIGERATED, AMBIENT): ");
                            String temperatureTypeAdd = commandReader.nextLine();
                            controller.addShelvingUnit(positionAdd, temperatureTypeAdd);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Added successfully!" + RESET);
                            break;
                        case "removeShelvingUnit":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the position of the shelving unit to remove (integer): ");
                            int positionErase = Integer.parseInt(commandReader.nextLine());
                            controller.removeShelvingUnit(positionErase);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Removed successfully!" + RESET);
                            break;
                        case "swapShelvingUnits":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the first shelving unit position (integer): ");
                            int position1Swap = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the second shelving unit position (integer): ");
                            int position2Swap = Integer.parseInt(commandReader.nextLine());
                            controller.swapShelvingUnits(position1Swap, position2Swap);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Swapped successfully!" + RESET);
                            break;
                        case "emptyShelvingUnit":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the position of the shelving unit to empty (integer): ");
                            int positionEmpty = Integer.parseInt(commandReader.nextLine());
                            controller.emptyShelvingUnit(positionEmpty);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Emptied successfully!" + RESET);
                            break;
                        case "createProduct":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the product name (String): ");
                            String name = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the temperature type (FROZEN, REFRIGERATED, AMBIENT): ");
                            String temperatureTypeCreate = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the price (float): ");
                            float price = Float.parseFloat(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the image path (String): ");
                            String imgPath = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Number of keywords (integer): ");
                            int nbKeyWords = Integer.parseInt(commandReader.nextLine());
                            ArrayList<String> keyWords = new ArrayList<>();
                            for (int i = 0; i < nbKeyWords; i++) {
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Key word " + (i + 1) + " (String): ");
                                String keyWord = commandReader.nextLine();
                                keyWords.add(keyWord);
                            }
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Number of related products (integer): ");
                            int nbRelatedProducts = Integer.parseInt(commandReader.nextLine());
                            ArrayList<String> relatedProducts = new ArrayList<>();
                            ArrayList<Float> relatedValues = new ArrayList<>();
                            for (int i = 0; i < nbRelatedProducts; i++) {
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Related product name " + (i + 1) + " (String): ");
                                String relatedProductName = commandReader.nextLine();
                                relatedProducts.add(relatedProductName);
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Relation value with product " + (i + 1) + " (float): ");
                                float relatedValue = Float.parseFloat(commandReader.nextLine());
                                relatedValues.add(relatedValue);
                            }
                            controller.createProduct(name, temperatureTypeCreate, price, imgPath, keyWords, relatedProducts, relatedValues);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Product created successfully!" + RESET);
                            break;
                        case "removeProduct":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the product name to remove (String): ");
                            String productNameErase = commandReader.nextLine();
                            controller.removeProduct(productNameErase);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Product removed successfully!" + RESET);
                            break;
                        case "modifyProduct":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the product name to modify (String): ");
                            String nameModify = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the new temperature type (FROZEN, REFRIGERATED, AMBIENT): ");
                            String temperatureProductModify = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the new price (float): ");
                            float priceModify = Float.parseFloat(commandReader.nextLine());
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the new image path (String): ");
                            String imgPathModify = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Number of keywords (integer): ");
                            int nbKeyWordsModify = Integer.parseInt(commandReader.nextLine());
                            ArrayList<String> keyWordsModify = new ArrayList<>();
                            for (int i = 0; i < nbKeyWordsModify; i++) {
                                normalOutput.print(BLUE + PROMPT_EMOJI + " Key word " + (i + 1) + " (String): ");
                                String keyWord = commandReader.nextLine();
                                keyWordsModify.add(keyWord);
                            }
                            controller.modifyProduct(nameModify, temperatureProductModify, priceModify, imgPathModify, keyWordsModify);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Product modified successfully!" + RESET);
                            break;
                        case "modifyProductRelation":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the first product name (String): ");
                            String productName1 = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the second product name (String): ");
                            String productName2 = commandReader.nextLine();
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the relation value (float): ");
                            float relation = Float.parseFloat(commandReader.nextLine());
                            controller.modifyProductRelation(productName1, productName2, relation);
                            normalOutput.println(GREEN + SUCCESS_EMOJI + " Product relation modified successfully!" + RESET);
                            break;
                        case "searchProduct":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the search text (String): ");
                            String searchText = commandReader.nextLine();
                            List<Product> products;
                            products = controller.searchProduct(searchText);
                            normalOutput.println(YELLOW + "Search Result:" + RESET);
                            normalOutput.println(YELLOW + "=======================================");
                            for (Product product : products) {
                                normalOutput.println(YELLOW + product.getInfo());
                            }
                            normalOutput.println(YELLOW + "=======================================");
                            break;
                        case "getSupermarketInfo":
                            normalOutput.print(YELLOW + controller.getSupermarketInfo());
                            break;
                        case "getCatalogInfo":
                            normalOutput.print(YELLOW + controller.getCatalogInfo());
                            break;
                        case "getShelvingUnitInfo":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the shelving unit position (integer): ");
                            int position = Integer.parseInt(commandReader.nextLine());
                            normalOutput.print(YELLOW + controller.getShelvingUnitInfo(position));
                            break;
                        case "getProductInfo":
                            normalOutput.print(BLUE + PROMPT_EMOJI + " Please enter the product name (String): ");
                            String productNameInfo = commandReader.nextLine();
                            normalOutput.print(YELLOW + controller.getProductInfo(productNameInfo));
                            break;
                        default:
                            if (!command.isEmpty()) normalOutput.println(RED + ERROR_EMOJI + " Invalid command" + RESET);
                            break;
                        }
                    }
                } catch (Exception e) {
                    errOutput.println("\n" + RED + ERROR_EMOJI + " " + e.getMessage() + RESET);
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
    private static String helperInfo() {
        return
                YELLOW + "Available Commands:\n" +
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
                        "BruteForce\n" +
                        "Approximation\n" +
                        "sortSupermarketProducts - Sorts supermarket products based on the specified strategy.\n" +
                        "The strategies can be:\n" +
                        "BruteForce\n" +
                        "Approximation\n" +
                        "addProductToShelvingUnit - Adds a product to a shelving unit.\n" +
                        "removeProductFromShelvingUnit - Removes a product from a shelving unit.\n" +
                        "swapProductsFromShelvingUnits - Swaps products between two shelving units.\n" +
                        "modifyShelvingUnitType - Modifies the type (temperature) of a shelving unit.\n" +
                        "The types can be:\n" +
                        "FROZEN\n" +
                        "REFRIGERATED\n" +
                        "AMBIENT\n" +
                        "addShelvingUnit - Adds a new shelving unit.\n" +
                        "removeShelvingUnit - Removes a shelving unit.\n" +
                        "swapShelvingUnits - Swaps two shelving units.\n" +
                        "emptyShelvingUnit - Empties the contents of a shelving unit.\n" +
                        "createProduct - Creates a new product and adds it to the catalog.\n" +
                        "removeProduct - Erases a product from the catalog.\n" +
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
