package edu.upc.subgrupprop113.supermarketmanager;

import java.util.List;
import java.util.ArrayList;

public class Catalog {

    private static Catalog catalog;
    private List<Product> products;

    public static Catalog getCatalog() {
        if (catalog == null) {
            catalog = new Catalog();
        }
        return catalog;
    }
}
