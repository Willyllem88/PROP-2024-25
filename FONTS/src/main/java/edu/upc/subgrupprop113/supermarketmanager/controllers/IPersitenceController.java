package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

public interface IPersitenceController {
    SupermarketData importSupermarket(String filePath);
    void exportSupermarket(SupermarketData supermarketData, String filePath);
}
