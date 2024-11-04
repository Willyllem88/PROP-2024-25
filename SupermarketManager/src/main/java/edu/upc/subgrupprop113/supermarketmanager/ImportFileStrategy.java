package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface ImportFileStrategy {
    //Instead of void should return a json file
    public abstract Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> importSupermarket(String filePath);
    public abstract ArrayList<Product> importCatalog(String filePath);
    public abstract ArrayList<ShelvingUnit> importShelvingUnits(String filePath);
}
