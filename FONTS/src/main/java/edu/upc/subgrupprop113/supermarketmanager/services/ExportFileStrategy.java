package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

public interface ExportFileStrategy {
    //Instead of void should return a json file
    void exportSupermarket(SupermarketData supermarketData, String filePath);
}
