package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

public interface OrderingStrategy {
    public abstract ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> shelvingUnits, List<Product> products);
}
