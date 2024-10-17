package edu.upc.subgrupprop113.supermarketmanager;

import java.util.List;
import java.util.ArrayList;

public class Supermarket {

    private static Supermarket instance;
    private final List<User> users;
    private User currentUser;
    private int shelvingHeight;
    private List<ShelvingUnit> shelvingUnits;

    // Usernames and passwords for both admin and user
    private static final String ADMIN_NAME = "Juan";
    private static final String ADMIN_PASSWORD = "qwerty";
    private static final String EMPLOYEE_NAME = "María";
    private static final String EMPLOYEE_PASSWORD = "1234567";

    private Supermarket() {
        users = new ArrayList<>();
        users.add(new Admin(ADMIN_NAME, ADMIN_PASSWORD));
        users.add(new User(EMPLOYEE_NAME, EMPLOYEE_PASSWORD));

        currentUser = null;

        shelvingUnits = new ArrayList<>();
    }

    public static Supermarket getInstance() {
        if (instance == null)
            instance = new Supermarket();

        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void logIn(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.isPasswordCheck(password)) {
                currentUser = user;
            }
        }
    }
}
