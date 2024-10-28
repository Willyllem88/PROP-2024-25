package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;

public class Supermarket {

    private static Supermarket instance;
    private OrderingStrategy orderingStrategy;
    private ImportFileStrategy importFileStrategy;
    private ExportFileStrategy exportFileStrategy;
    private ArrayList<User> registeredUsers;
    private User logedUser;
    private Catalog catalog;
    private ArrayList<ShelvingUnit> shelvingUnits;

    // Usernames and passwords for both admin and user
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EMPLOYEE_NAME = "employee";
    private static final String EMPLOYEE_PASSWORD = "employee";

    private Supermarket() {

    }

    public static Supermarket getInstance() {
        if (instance == null) {
            instance = new Supermarket();
        }
        return instance;
    }
}
