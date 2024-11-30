package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

public interface ImportFileStrategy {
    //Instead of void should return a json file
    public abstract SupermarketData importSupermarket(String filePath);
}
