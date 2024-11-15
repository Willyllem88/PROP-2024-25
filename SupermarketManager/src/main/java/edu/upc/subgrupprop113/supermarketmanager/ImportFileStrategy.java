package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface ImportFileStrategy {
    //Instead of void should return a json file
    public abstract SupermarketData importSupermarket(String filePath);
}
