package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class SupermarketData {
    private ArrayList<Product> products;
    private ArrayList<ShelvingUnit> distribution;
    private ArrayList<User> registeredUsers;

    @JsonCreator
    public SupermarketData(@JsonProperty("products") ArrayList<Product> products,
                           @JsonProperty("distribution") ArrayList<ShelvingUnit> distribution,
                           @JsonProperty("registeredUsers") ArrayList<User> registeredUsers) {
        this.products = products;
        this.distribution = distribution;
        this.registeredUsers = registeredUsers;
    }

    public ArrayList<Product> getProducts() { return products; }
    public void setProducts(ArrayList<Product> products) { this.products = products; }

    public ArrayList<ShelvingUnit> getDistribution() { return distribution; }
    public void setDistribution(ArrayList<ShelvingUnit> distribution) { this.distribution = distribution; }

    public ArrayList<User> getRegisteredUsers() { return registeredUsers; }
    public void setRegisteredUsers(ArrayList<User> registeredUsers) { this.registeredUsers = registeredUsers; }

    public void print() {
        // Print supermarket general information
        System.out.println("==================================== Supermarket Overview =====================================");
        System.out.println("Products: " + products.size());
        System.out.println("Shelving Units: " + distribution.size());
        System.out.println("Registered Users: " + registeredUsers.size());
        System.out.println("--------------------------------------------------------------------------------------------");

        // Print Shelving Units
        System.out.println("------ Shelving Units -------");
        for (ShelvingUnit shelvingUnit : distribution) {
            System.out.println("UID: " + shelvingUnit.getUid());
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
