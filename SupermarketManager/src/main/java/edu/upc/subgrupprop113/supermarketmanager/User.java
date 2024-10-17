package edu.upc.subgrupprop113.supermarketmanager;

public class User {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Boolean isPasswordCheck(String password) {
        return this.password.equals(password);
    }

    public Boolean isAdmin() {
        return false;
    }
}
