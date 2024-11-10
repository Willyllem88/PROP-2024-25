package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * The SupermarketData class represents the main data structure for a supermarket management system.
 * It holds information about products, shelving units, registered users, and provides a method to print an overview
 * of the supermarket’s data, including relationships between products and user details.
 */
public class SupermarketData {
    private int shelvingUnitHeight;
    private ArrayList<Product> products;
    private ArrayList<ShelvingUnit> distribution;
    private ArrayList<User> registeredUsers;

    /**
     * Constructs a new SupermarketData instance with the specified height for shelving units,
     * list of products, distribution of shelving units, and list of registered users.
     *
     * @param shelvingUnitHeight the height of each shelving unit
     * @param products the list of products available in the supermarket
     * @param distribution the list of shelving units in the supermarket
     * @param registeredUsers the list of users registered in the supermarket system
     */
    @JsonCreator
    public SupermarketData(@JsonProperty("shelvingUnitHeight") int shelvingUnitHeight,
                           @JsonProperty("products") ArrayList<Product> products,
                           @JsonProperty("distribution") ArrayList<ShelvingUnit> distribution,
                           @JsonProperty("registeredUsers") ArrayList<User> registeredUsers) {
        this.shelvingUnitHeight = shelvingUnitHeight;
        this.products = products;
        this.distribution = distribution;
        this.registeredUsers = registeredUsers;
    }

    /**
     * Returns the height of each shelving unit.
     *
     * @return the shelving unit height.
     */
    public int getShelvingUnitHeight() { return shelvingUnitHeight; }

    /**
     * Sets the height of the shelving units.
     *
     * @param shelvingUnitHeight the height to set for each shelving unit.
     */
    public void setShelvingUnitHeight(int shelvingUnitHeight) {}

    /**
     * Returns the list of products in the supermarket.
     *
     * @return the list of products.
     */
    public ArrayList<Product> getProducts() { return products; }

    /**
     * Sets the list of products in the supermarket.
     *
     * @param products the list of products to set.
     */
    public void setProducts(ArrayList<Product> products) { this.products = products; }

    /**
     * Returns the list of shelving units in the supermarket.
     *
     * @return the list of shelving units.
     */
    public ArrayList<ShelvingUnit> getDistribution() { return distribution; }

    /**
     * Sets the list of shelving units in the supermarket.
     *
     * @param distribution the list of shelving units to set.
     */
    public void setDistribution(ArrayList<ShelvingUnit> distribution) { this.distribution = distribution; }

    /**
     * Returns the list of users registered in the supermarket system.
     *
     * @return the list of registered users.
     */
    public ArrayList<User> getRegisteredUsers() { return registeredUsers; }

    /**
     * Sets the list of users registered in the supermarket system.
     *
     * @param registeredUsers the list of registered users to set.
     */
    public void setRegisteredUsers(ArrayList<User> registeredUsers) { this.registeredUsers = registeredUsers; }

    /**
     * Prints an overview of the supermarket's data, including:
     * - General information about the number of products, shelving units, and registered users.
     * - Details of each shelving unit, including its height, temperature, and associated products.
     * - All unique relationships between products.
     * - Information on registered users, categorized by their roles (Admin or Employee).
     */
    public void print() {
        // Print supermarket general information
        System.out.println("==================================== Supermarket Overview =====================================");
        System.out.println("Products: " + products.size());
        System.out.println("Shelving Units: " + distribution.size());
        System.out.println("Shelving Unit Height: " + shelvingUnitHeight);
        System.out.println("Registered Users: " + registeredUsers.size());
        System.out.println("--------------------------------------------------------------------------------------------");

        // Print Shelving Units
        System.out.println("------ Shelving Units -------");
        for (ShelvingUnit shelvingUnit : distribution) {
            System.out.println("UID: " + shelvingUnit.getUid());
            System.out.println("Height: " + shelvingUnit.getHeight());
            System.out.println("Temperature: " + shelvingUnit.getTemperature());
            System.out.println("Products: " + shelvingUnit.getProducts().size());
            System.out.println("  ------------------------------------------------------");

            // Print products for each shelving unit
            for (Product product : shelvingUnit.getProducts()) {
                if (product != null) {
                    System.out.println("    Product: " + product.getName());
                    System.out.println("    Price: " + String.format("%.2f", product.getPrice()) + " USD");
                    System.out.println("    Temperature: " + product.getTemperature());
                    System.out.println("    Image Path: " + product.getImgPath());
                    System.out.println("    Key Words: " + product.getKeyWords());
                }
                else {
                    System.out.println("    EMPTY SHELF");
                }
                System.out.println("    ------------------------------------------------------");
            }
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("--------------------------------------------------------------------------------------------");

        // Print all the unique Relationships between products
        System.out.println("------ Product Relationships -------");
        Set<RelatedProduct> allRelationships = new HashSet<>();
        for (Product product : products) {
            allRelationships.addAll(product.getRelatedProducts());
        }
        for (RelatedProduct relatedProduct : allRelationships) {
            System.out.println(relatedProduct.getProduct1().getName() + " - " + relatedProduct.getProduct2().getName() + ": (" + relatedProduct.getValue() + ")");
        }

        System.out.println("--------------------------------------------------------------------------------------------");

        // Print Users (Admins and Employees)
        System.out.println("------ Registered Users -------");
        for (User user : registeredUsers) {
            String userType = (user instanceof Admin) ? "Admin" : (user instanceof Employee) ? "Employee" : "Unknown";
            System.out.println("User Type: " + userType);
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password Hash: " + user.getPasswordHash());  // You might want to hide the password in production!
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("============================================================================================");
    }
}
