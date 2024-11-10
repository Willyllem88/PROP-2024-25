package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ImportFileStub implements ImportFileStrategy{

    @Override
    public Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> importSupermarket(String filePath) {
        Product bread = new Product("bread", 0.4f, ProductTemperature.AMBIENT, "path/to/img");
        Product water = new Product("water", 0.4f, ProductTemperature.AMBIENT, "path/to/img");

        ShelvingUnit unit0 = new ShelvingUnit(0, 2, ProductTemperature.AMBIENT);
        ShelvingUnit unit1 = new ShelvingUnit(1, 2, ProductTemperature.FROZEN);
        ShelvingUnit unit2 = new ShelvingUnit(2, 4, ProductTemperature.FROZEN);

        ArrayList<Product> products = new ArrayList<>();
        products.add(bread);

        ArrayList<ShelvingUnit> unitsDiffTemp = new ArrayList<>();
        unitsDiffTemp.add(unit0);
        unitsDiffTemp.add(unit1);
        unitsDiffTemp.getLast().addProduct(bread, 0);

        ArrayList<ShelvingUnit> unitsProductNotContained= new ArrayList<>();
        unitsProductNotContained.add(unit0);
        unitsDiffTemp.getFirst().addProduct(water, 0);

        ArrayList<ShelvingUnit> unitsDiffHeights= new ArrayList<>();
        unitsDiffHeights.add(unit0);
        unitsDiffHeights.add(unit2);

        ArrayList<ShelvingUnit> unitsDuppUids = new ArrayList<>();
        unitsDuppUids.add(unit1);
        unitsDuppUids.add(unit1);

        ArrayList<ShelvingUnit> unitsCorrect = new ArrayList<>();
        unitsCorrect.add(unit0);
        unitsCorrect.add(unit1);

        return switch (filePath) {
            case "different/temps" -> new Pair<>(products, unitsDiffTemp);
            case "product/not/contained" -> new Pair<>(products, unitsProductNotContained);
            case "different/heights" -> new Pair<>(products, unitsDiffHeights);
            case "dupplicated/uids" -> new Pair<>(products, unitsDuppUids);
            default ->
                    new Pair<ArrayList<Product>, ArrayList<ShelvingUnit>>(products, unitsCorrect);
        };
    }

    @Override
    public ArrayList<Product> importCatalog(String filePath) {
        return new ArrayList<Product>();
    }

    @Override
    public ArrayList<ShelvingUnit> importShelvingUnits(String filePath) {
        return new ArrayList<ShelvingUnit>();
    }
}
