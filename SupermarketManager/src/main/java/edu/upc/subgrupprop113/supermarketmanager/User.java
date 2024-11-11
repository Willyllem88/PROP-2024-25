package edu.upc.subgrupprop113.supermarketmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents an abstract user within the supermarket management system.
 * This class serves as a base for specific types of users, storing common attributes
 * like username and password.
 */
public abstract class User {
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

    @JsonCreator
    public static User createUser(
            @JsonProperty("userType") String userType,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        if ("Admin".equalsIgnoreCase(userType)) {
            return new Admin(username, password);  // Create Admin object
        } else if ("Employee".equalsIgnoreCase(userType)) {
            return new Employee(username, password);  // Create Employee object
        } else {
            throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

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
