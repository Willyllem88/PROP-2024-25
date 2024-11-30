package edu.upc.subgrupprop113.supermarketmanager.models;

/**
 * Represents a standard employee user within the supermarket management system.
 * This class extends {@link User} but does not provide administrative privileges.
 */
public class Employee extends User {

    /**
     * Constructs a new Employee user with the specified username and password.
     *
     * @param username the unique identifier for the employee.
     * @param password the password for the employee's account.
     */
    public Employee(String username, String password) {
        super(username, password);
    }
}
