package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;

public class DomainController {
    private static DomainController instance;
    private Supermarket supermarket;
    private Catalog catalog;

    public static DomainController getInstance() {
        if (instance == null) {
            instance = new DomainController();
        }
        return instance;
    }

    private DomainController() {
        supermarket = Supermarket.getInstance();
        catalog = Catalog.getInstance();
    }

    public void logIn(String username, String password) {
        supermarket.logIn(username, password);
    }

    public void logOut() {
        supermarket.logOut();
    }
}
