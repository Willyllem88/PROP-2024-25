package edu.upc.subgrupprop113.supermarketmanager.models;

/**
 * Represents an abstract user within the supermarket management system.
 * This class serves as a base for specific types of users, storing common attributes
 * like username and password.
 */
public class User {
    private final String username;
    private final String password;

    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username the unique identifier for the user.
     * @param password the password for the user account.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the hash of the user's password.
     *
     * @return the hash of the user's password.
     */
    public int getPasswordHash() {
        return password.hashCode();
    }

    /**
     * Verifies if the provided password matches the user's password.
     *
     * @param password the password to be checked against the user's stored password.
     * @return {@code true} if the password matches; {@code false} otherwise.
     */
    public Boolean isPasswordCheck(String password) {
        return this.password.equals(password);
    }

    /**
     * Determines if the user has administrator privileges. By default, this returns {@code false}.
     * Subclasses may override this method to define administrative users.
     *
     * @return {@code true} if the user is an administrator; {@code false} otherwise.
     */
    public Boolean isAdmin() {
        return false;
    }
}
