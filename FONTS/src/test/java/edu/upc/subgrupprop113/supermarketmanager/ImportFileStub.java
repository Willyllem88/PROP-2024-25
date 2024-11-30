package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileStrategy;

import java.util.ArrayList;

public class ImportFileStub implements ImportFileStrategy {

    @Override
    public SupermarketData importSupermarket(String filePath) {
        Product bread = new Product("bread", 0.4f, ProductTemperature.AMBIENT, "path/to/img");
        Product water = new Product("water", 0.4f, ProductTemperature.AMBIENT, "path/to/img");

        ShelvingUnit unit0 = new ShelvingUnit(0, 2, ProductTemperature.AMBIENT);
        ShelvingUnit unit1 = new ShelvingUnit(1, 2, ProductTemperature.FROZEN);
        ShelvingUnit unit2 = new ShelvingUnit(2, 4, ProductTemperature.FROZEN);

        ArrayList<Product> products = new ArrayList<>();
        products.add(bread);

        ArrayList<ShelvingUnit> unitsDiffTemp = new ArrayList<>();
        ArrayList<ShelvingUnit> unitsProductNotContained= new ArrayList<>();
        ArrayList<ShelvingUnit> unitsDiffHeights= new ArrayList<>();
        ArrayList<ShelvingUnit> unitsDuppUids = new ArrayList<>();
        ArrayList<ShelvingUnit> unitsCorrect = new ArrayList<>();

        switch (filePath) {
            case "product/not/contained": {
                unitsProductNotContained.add(unit0);
                unitsProductNotContained.getFirst().addProduct(water, 0);
                return new SupermarketData(2, products, unitsProductNotContained);
            }
            case "different/heights": {
                unitsDiffHeights.add(unit0);
                unitsDiffHeights.add(unit2);
                return new SupermarketData(2, products, unitsDiffHeights);
            }
            case "dupplicated/uids": {
                unitsDuppUids.add(unit1);
                unitsDuppUids.add(unit1);
                return new SupermarketData(2, products, unitsDuppUids);
            }
            default: {
                unitsCorrect.add(unit0);
                unitsCorrect.add(unit1);
                unitsCorrect.getFirst().addProduct(bread, 0);
                return new SupermarketData(2, products, unitsCorrect);
            }
        }
    }
}
