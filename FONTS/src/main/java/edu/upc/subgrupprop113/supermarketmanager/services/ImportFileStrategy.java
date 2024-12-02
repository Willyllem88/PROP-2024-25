package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

public interface ImportFileStrategy {
    //Instead of void should return a json file
    SupermarketData importSupermarket(String filePath);
}
