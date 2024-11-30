package edu.upc.subgrupprop113.supermarketmanager.models;

/**
 * Represents an administrator user within the supermarket management system.
 * This class extends {@link User} and overrides the {@code isAdmin} method to
 * indicate that the user has administrative privileges.
 */
public class Admin extends User {

    /**
     * Constructs a new Admin user with the specified username and password.
     *
     * @param username the unique identifier for the administrator user.
     * @param password the password for the administrator account.
     */
    public Admin(String username, String password) {
        super(username, password);
    }

    /**
     * Indicates that this user has administrator privileges.
     *
     * @return {@code true}, as this user is an administrator.
     */
    @Override
    public Boolean isAdmin() {
        return true;
    }
}
