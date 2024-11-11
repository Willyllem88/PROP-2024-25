package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

public interface OrderingStrategy {
    public abstract ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> shelvingUnits, List<Product> products);
}
