package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.Set;

public interface OrderingStrategy {
    public abstract ArrayList<ShelvingUnit> orderSupermarket(ArrayList<ShelvingUnit> shelvingUnits, Set<Product> products);
}
