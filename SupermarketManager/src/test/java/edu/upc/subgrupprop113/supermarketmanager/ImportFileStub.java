package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ImportFileStub implements ImportFileStrategy{

    @Override
    public Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> importSupermarket(String filePath) {
        return new Pair<ArrayList<Product>, ArrayList<ShelvingUnit>>(new ArrayList<Product>(), new ArrayList<ShelvingUnit>());
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
