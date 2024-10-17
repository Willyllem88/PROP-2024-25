package edu.upc.subgrupprop113.supermarketmanager;

public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public Boolean isAdmin() {
        return true;
    }
}
