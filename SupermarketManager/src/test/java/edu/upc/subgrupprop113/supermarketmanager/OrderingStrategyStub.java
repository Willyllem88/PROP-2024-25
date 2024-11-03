package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.Set;

public class OrderingStrategyStub implements OrderingStrategy {
    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(ArrayList<ShelvingUnit> shelvingUnits, Set<Product> products) {
        ArrayList<ShelvingUnit> orderedShelvingUnits = new ArrayList<>();
        Product product1 = new Product("bread", 10.0f, ProductTemperature.AMBIENT, "path");
        Product product2 = new Product("water", 10.0f, ProductTemperature.REFRIGERATED, "path");

        orderedShelvingUnits.add(new ShelvingUnit(0, 2, ProductTemperature.FROZEN));
        orderedShelvingUnits.add(new ShelvingUnit(1, 2, ProductTemperature.REFRIGERATED));
        orderedShelvingUnits.add(new ShelvingUnit(2, 2, ProductTemperature.REFRIGERATED));
        orderedShelvingUnits.add(new ShelvingUnit(3, 2, ProductTemperature.AMBIENT));

        orderedShelvingUnits.get(1).addProduct(product2, 0);
        orderedShelvingUnits.get(3).addProduct(product1, 0);

        return orderedShelvingUnits;
    }
}
