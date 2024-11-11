package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface OrderingStrategy {
    public abstract ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> shelvingUnits, List<Product> products);
}
