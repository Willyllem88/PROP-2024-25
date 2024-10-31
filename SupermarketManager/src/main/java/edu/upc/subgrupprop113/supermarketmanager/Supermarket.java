package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;

/**
 * Represents the supermarket system, managing all operations related to users,
 * catalog, shelving units, and various strategies for ordering and file import/export.
 * Implements a singleton pattern to ensure only one instance of the supermarket is created.
 */
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

    /**
     * Private constructor to initialize the supermarket with default strategies and users.
     * Sets default instances for ordering, catalog, import/export strategies,
     * and adds initial users (admin and employee).
     */
    private Supermarket() {
        this.orderingStrategy = new BruteForce();
        this.catalog = Catalog.getInstance();
        this.shelvingUnits = new ArrayList<ShelvingUnit>();
        this.importFileStrategy = new ImportFileExampleStrategy();
        this.exportFileStrategy = new ExportFileExampleStrategy();
        this.registeredUsers = new ArrayList<>();
        this.registeredUsers.add(new Employee(EMPLOYEE_NAME, EMPLOYEE_PASSWORD));
        this.registeredUsers.add(new Admin(ADMIN_NAME, ADMIN_PASSWORD));

        this.logedUser = null;
    }

    /**
     * Returns the singleton instance of the supermarket, creating it if it does not exist.
     *
     * @return the single instance of {@code Supermarket}.
     */
    public static Supermarket getInstance() {
        if (instance == null) {
            instance = new Supermarket();
        }
        return instance;
    }

    /**
     * Logs a user into the system with the specified username and password.
     * Verifies that no other user is already logged in and checks the provided credentials.
     *
     * @param username the username of the user attempting to log in.
     * @param password the password of the user attempting to log in.
     * @throws IllegalStateException if a user is already logged in.
     * @throws IllegalArgumentException if the username does not exist or if the password is incorrect.
     */
    public void logIn(String username, String password) {
        if (this.logedUser != null) throw new IllegalStateException("There is already a logged in user.");

        User user = findUser(username);
        if (user == null) throw new IllegalArgumentException("No such user found.");
        if (!user.isPasswordCheck(password)) throw new IllegalArgumentException("Wrong password.");

        this.logedUser = user;
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @throws IllegalStateException if no user is currently logged in.
     */
    public void logOut() {
        if (this.logedUser == null) throw new IllegalStateException("There is no logged user.");

        this.logedUser = null;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the {@link User} who is currently logged in, or {@code null} if no user is logged in.
     */
    public User getLogedUser() {
        return this.logedUser;
    }

    /**
     * Searches for a user by their username within the registered users.
     *
     * @param username the username of the user to search for.
     * @return the {@link User} object if found; {@code null} if no user with the specified username exists.
     */
    public User findUser(String username) {
        for (User u : registeredUsers)
            if (u.getUsername().equals(username)) return u;
        return null;
    }

}
